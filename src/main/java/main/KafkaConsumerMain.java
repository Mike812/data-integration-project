package main;

import company.*;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import utils.CustomerEventDeserializer;
import utils.InputOutputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class KafkaConsumerMain {

    public static void main(String[] args){

        Options options = new Options();
        Option logDirOption = new Option("l", "log_dir", true, "directory for log files");
        logDirOption.setRequired(true);
        options.addOption(logDirOption);

        String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
        String logTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
        Logger logger = Logger.getLogger("Kafka Consumer Main Log");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        FileHandler fh;
        try {
            CommandLine cmd = parser.parse(options, args);
            String logDir = cmd.getOptionValue("log_dir");
            String logFile = logDir + "/" + KafkaConsumerMain.class.getSimpleName() + "_" + logTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

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

            String database = "company";
            SqlStatements sqlStatements = SqlStatementsFactory.getSqlStatementsObject(database, logger);

            List<CustomerEvent> customerEvents = new ArrayList<>();
            int databaseThreshold = 20000;

            while (true){
                ConsumerRecords<String, CustomerEvent> consumerRecords = consumer.poll(Duration.ofMillis(100));
                //logger.info("Number of polled consumer records: " + consumerRecords.count());
                String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
                for (ConsumerRecord<String, CustomerEvent> record : consumerRecords) {
                    CustomerEvent customerEvent = record.value();
                    customerEvents.add(customerEvent);
                    //System.out.println("Key: " + record.key() + " Value: " + customerEvent + " Partition: " + record.partition() +
                      //      " Offset: " + record.offset());
                }

                if(customerEvents.size() >= databaseThreshold){
                    logger.info("Process collected customer event list with size: " + customerEvents.size());
                    List<CustomerEvent> customerEventsAggregated = CustomerEventFactory.getListWithSummedUpSalesAmounts(customerEvents, currentTimestamp);
                    int maxId = sqlStatements.getMaxIdFromTable(CustomerEventTable.TABLE_NAME, CustomerEventTable.ID_COLUMN);
                    List<CustomerEvent> customerEventsWithId = CustomerEventFactory.addIdToCustomerEvents(customerEventsAggregated, maxId);
                    int result = sqlStatements.insertCustomerEventsIntoTable(CustomerEventTable.TABLE_NAME, customerEventsWithId);
                    if(result == 0){
                        logger.info("Successfully inserted aggregated customer events");
                    }
                    customerEvents = new ArrayList<>();
                }
                // commit offsets after processing of data
                consumer.commitSync();
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            logger.info(e.getMessage());
            formatter.printHelp("Kafka Consumer Main", options);        }
    }
}
