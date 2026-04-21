package main;

import company.CustomerEvent;
import company.EmployeeTable;
import org.apache.spark.sql.*;
import utils.CompanyDatabaseConnection;
import utils.PostgreSqlUtils;

import java.util.Properties;

public class SparkMain {

    public static void main(String[] args){
        SparkSession sparkSession = SparkSession.builder()
                .appName("Spark Main")
                .master("local")
                .getOrCreate();

        Properties postgresProps = PostgreSqlUtils.getPostgresProps();
        String url = postgresProps.getProperty("url") + CompanyDatabaseConnection.getDatabaseName();

        Properties sparkJdbcProps = new Properties();
        sparkJdbcProps.put("user", postgresProps.get("user.name"));
        sparkJdbcProps.put("password", postgresProps.get("password"));
        sparkJdbcProps.put("driver", "org.postgresql.Driver");

        Dataset<Row> df = sparkSession.read().jdbc(url, EmployeeTable.TABLE_NAME, sparkJdbcProps);

        df.show();

        Encoder<CustomerEvent> customerEventEncoder = Encoders.bean(CustomerEvent.class);

        Dataset<Row> lines = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "customer_events_topic")
                .option("startingOffsets", "earliest")
                .load()
                .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

        lines.show();

        Dataset<CustomerEvent> ds = lines.as(customerEventEncoder);
        ds.show();
    }
}
