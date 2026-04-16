FROM apache/kafka:latest

RUN apt-get update
RUN apt-get install nano
RUN apt-get install default-jre -y

RUN mkdir -p /home/logs
RUN mkdir -p /home/sample_data
RUN mkdir -p /home/sample_data/employee
RUN mkdir -p /home/sample_data/customer_event
RUN chmod 777 -R /home

# COPY ../target/database-project-1.0-jar-with-dependencies.jar /home/database-project.jar
COPY setup_kafka_topic.sh /home/setup_kafka_topic.sh

WORKDIR /home
