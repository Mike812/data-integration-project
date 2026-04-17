package main;

import company.CustomerEvent;
import org.apache.spark.sql.*;

public class SparkStreamingMain {

    public static void main(String[] args){
        SparkSession sparkSession = SparkSession.builder()
                .appName("Spark Streaming Test")
                .master("local")
                .getOrCreate();

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
