#!/bin/bash

export SPARK_HOME=/opt/spark-4.1.0-bin-hadoop3
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
export KAFKA_HOME=/opt/kafka_2.13-3.8.0
export PATH=$KAFKA_HOME/bin:$PATH

$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties &
echo "Waiting for 20 seconds before proceeding with creating kafka topic and starting java programs" && sleep 20
$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic customer_events_topic
# $KAFKA_HOME/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic customer_events_topic
# $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic customer_events_topic --from-beginning

java -cp data-integration-project.jar main.SetupTablesMain -d company -l /home/logs
java -cp data-integration-project.jar main.SampleDataMain -t employee -l /home/logs -o /home/sample_data/employee -n 1000
java -cp data-integration-project.jar main.SampleDataMain -t customer_event -l /home/logs -o /home/sample_data/customer_event -n 10000

echo "Waiting for 10 seconds before proceeding with next java programs" && sleep 10
java -cp data-integration-project.jar main.SampleDataMain -t customer_event -l /home/logs -o /home/sample_data/customer_event -n 10000
java -cp data-integration-project.jar main.SampleDataMain -t employee -l /home/logs -o /home/sample_data/employee -n 1000

echo "Waiting for 10 seconds before proceeding with next java programs" && sleep 10
java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t employee -i /home/sample_data/employee
# java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t customer_event -i /home/sample_data/customer_event
java -cp data-integration-project.jar main.KafkaProducerMain -i /home/sample_data/customer_event -l /home/logs
java -cp data-integration-project.jar main.KafkaConsumerMain -l /home/logs &

# spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.13:4.1.0 --class main.SparkStreamingMain --master local data-integration-project.jar
