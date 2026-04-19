FROM postgres:latest

RUN apt-get update
RUN apt-get install nano
RUN apt-get install default-jre -y

RUN mkdir -p /home/logs
RUN mkdir -p /home/sample_data
RUN mkdir -p /home/sample_data/employee
RUN mkdir -p /home/sample_data/customer_event
RUN chmod 777 -R /home

COPY target/kafka_2.13-3.8.0.tgz /opt
COPY target/spark-4.1.0-bin-hadoop3.tgz /opt
COPY target/data-integration-project-1.0-jar-with-dependencies.jar /home/data-integration-project.jar
COPY run_main_files.sh /home/run_main_files.sh

RUN tar xvf /opt/kafka_2.13-3.8.0.tgz -C /opt
RUN tar xvf /opt/spark-4.1.0-bin-hadoop3.tgz -C /opt

WORKDIR /home
