# Java data integration project

Java project that includes database connections to postgreSql and Streaming connections to Apache Kafka. 
It simulates a company with employees and customer events.
The idea is to create random sample data of employees and customer events and write it to json files.
The json files can be seen as input data that are processed and written to a postgreSql database or to a Kafka topic.
It is also possible to create different scenarios, e.g. read customer events from a database and publish it to a Kafka topic.

## Requirements

Install postgreSql and create a database that can be used as parameter in the main files.

Alternative 1: Pull the latest postgres docker image: docker pull postgres:latest <br>
Adapt the port in your properties file: url=jdbc:postgresql://localhost:7432/ <br>
Start your docker container as follows: <br>
```
docker run --name somePostgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=company -p 7432:5432 postgres:latest 
```
<br>
Alternative 2: Use the dockerfile in the project to copy the jar with dependencies to a container and run the java code there. <br>

```
docker build -t my-postgres-image .
docker run --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=company -p 7432:5432 my-postgres-image 
```
<br>

If you want to use the Kafka you can use the official Kafka image: <br>
```
docker run -d -p 9092:9092 --name kafkaBroker apache/kafka:latest
docker exec --workdir /opt/kafka/bin/ -it kafkaBroker sh
```
<br>
Alternatively you can use the dockerfile in the kafka directory: <br>

```
docker build -t my-kafka-image .
docker run -d -p 9092:9092 --name kafkaBroker my-kafka-image
```

## Setup tables main

Create tables in an existing database and insert random sample data.

usage: Setup tables main <br>
-c,--customer_events <arg> &emsp; number of customer event samples <br>
-d,--database <arg> &emsp; input database <br>
-e,--employees <arg> &emsp; number of employee samples <br>
-l,--log_dir <arg>     directory for log files <br>

## Sample data main

Create sample data for a given table in json format and write it to an output path.
Adapt the id accordingly if you run it multiple times.

usage: Sample data main <br>
-id,--start_id <arg>        start number of a sequential id <br>
-l,--log_dir <arg>          directory for log files <br>
-n,--number_samples <arg>   number of samples <br>
-o,--output <arg>           output path <br>
-t,--table <arg>            input table <br>

## Insert from directory main

Insert values from multiple json files in an input directory into a table. <br>

usage: Setup tables main <br>
-d,--database <arg> &emsp; input database <br>
-i,--input_dir <arg> &emsp; input directory with json files <br>
-l,--log_dir <arg>     directory for log files <br>
-t,--table <arg> &emsp; table in which values are inserted (employee or customer_event) <br>

