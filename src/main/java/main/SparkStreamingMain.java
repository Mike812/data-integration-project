package main;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkStreamingMain {

    public static void main(String[] args){
        SparkSession sparkSession = SparkSession.builder()
                .appName("Spark Streaming Test")
                .master("local")
                .getOrCreate();

        Dataset<Row> lines = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "customer_events_topic")
                .option("startingOffsets", "earliest").load();

        System.out.println(lines);
    }
}
