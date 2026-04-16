/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic customer_events_topic
#./kafka-console-producer.sh --bootstrap-server localhost:9092 --topic customer_events_topic
#./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic customer_events_topic --from-beginning