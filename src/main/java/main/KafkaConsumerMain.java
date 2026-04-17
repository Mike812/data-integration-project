package main;

import company.CustomerEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import utils.CustomerEventDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

public class KafkaConsumerMain {

    public static void main(String[] args){

        try {
            Properties kafkaProperties = new Properties();
            InputStream inputStream = KafkaConsumerMain.class.getClassLoader().getResourceAsStream("properties/kafka.properties");
            kafkaProperties.load(inputStream);
            String bootstrapServer = kafkaProperties.getProperty("bootstrap_server");
            String kafkaTopic = kafkaProperties.getProperty("kafka_topic");
            String consumerGroup = kafkaProperties.getProperty("consumer_group");

            // Create Consumer Properties
            Properties kafkaConsumerProperties = new Properties();
            kafkaConsumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            kafkaConsumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
            kafkaConsumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            kafkaConsumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerEventDeserializer.class.getName());
            kafkaConsumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            KafkaConsumer<String, CustomerEvent> consumer = new KafkaConsumer<>(kafkaConsumerProperties);
            consumer.subscribe(Collections.singletonList(kafkaTopic));

            while (true){
                ConsumerRecords<String, CustomerEvent> consumerRecords = consumer.poll(Duration.ofMillis(100));
                List<CustomerEvent> customerEvents = new ArrayList<>();
                for (ConsumerRecord<String, CustomerEvent> record : consumerRecords) {
                    CustomerEvent customerEvent = record.value();
                    customerEvents.add(customerEvent);
                    System.out.println("Key: " + record.key() +
                            " Value: " + customerEvent +
                            " Partition: " + record.partition() +
                            " Offset: " + record.offset()
                    );
                }
                //consumer.commitSync();
                Map<String, Integer> summedUpSalesAmounts = getMapWithSummedUpSalesAmounts(customerEvents);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getMapWithSummedUpSalesAmounts(List<CustomerEvent> customerEvents){
        Map<String, Integer> customerSalesMap = new HashMap<>();
        for(CustomerEvent customerEvent : customerEvents){
            String key = customerEvent.getCustomerName();
            if(customerSalesMap.get(key) != null){
                customerSalesMap.put(key, customerSalesMap.get(key) + customerEvent.getSalesAmount());
            }
            customerSalesMap.put(key, customerEvent.getSalesAmount());
        }

        return customerSalesMap;
    }
}
