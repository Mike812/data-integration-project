package main;

import company.CustomerEvent;
import company.CustomerEventFactory;
import company.SqlStatements;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import utils.CustomerEventDeserializer;
import utils.InputOutputUtils;
import utils.PostgreSqlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerMain {

    public static void main(String[] args){

        try {
            Properties kafkaProperties = new Properties();
            InputStream inputStream = KafkaConsumerMain.class.getClassLoader().getResourceAsStream("properties/kafka.properties");
            kafkaProperties.load(inputStream);
            String bootstrapServer = kafkaProperties.getProperty("bootstrap.server");
            String kafkaTopic = kafkaProperties.getProperty("kafka.topic");
            String consumerGroup = kafkaProperties.getProperty("consumer.group");

            // Create Consumer Properties
            Properties kafkaConsumerProperties = new Properties();
            kafkaConsumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            kafkaConsumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
            kafkaConsumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            kafkaConsumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomerEventDeserializer.class.getName());
            kafkaConsumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            KafkaConsumer<String, CustomerEvent> consumer = new KafkaConsumer<>(kafkaConsumerProperties);
            consumer.subscribe(Collections.singletonList(kafkaTopic));

            PostgreSqlUtils postgresSqlConnection = new PostgreSqlUtils("company");
            Connection connection = postgresSqlConnection.getPostgreSqlConnection();
            Statement statement = postgresSqlConnection.getSqlStatement(connection);
            //List<CustomerEvent> allCustomerEvents = JsonUtils.readCustomerEventsFromDirectory("C:\\data-integration-project\\sample_data\\json\\customer_event");

            while (true){
                ConsumerRecords<String, CustomerEvent> consumerRecords = consumer.poll(Duration.ofMillis(100));
                List<CustomerEvent> customerEvents = new ArrayList<>();
                String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
                String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
                for (ConsumerRecord<String, CustomerEvent> record : consumerRecords) {
                    CustomerEvent customerEvent = record.value();
                    customerEvents.add(customerEvent);
                    System.out.println("Key: " + record.key() +
                            " Value: " + customerEvent +
                            " Partition: " + record.partition() +
                            " Offset: " + record.offset()
                    );
                }

                if(!customerEvents.isEmpty()){
                    List<CustomerEvent> customerEventsAggregated = CustomerEventFactory.getListWithSummedUpSalesAmounts(customerEvents, currentTimestamp);
                    int result = SqlStatements.insertCustomerEventsIntoTable(connection, statement, customerEventsAggregated);
                    // commit offsets after processing of data
                    consumer.commitSync();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
