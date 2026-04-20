package main;

import company.CustomerEvent;
import company.CustomerEventFactory;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.CustomerEventSerializer;
import utils.InputOutputUtils;
import utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class KafkaProducerMain {

    public static void main(String[] args){

        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        Option inputDirOption = new Option("i", "input_dir", true, "input directory with json files");
        options.addOption(inputDirOption);

        Option logDirOption = new Option("l", "log_dir", true, "directory for log files");
        logDirOption.setRequired(true);
        options.addOption(logDirOption);

        Logger logger = Logger.getLogger("KafkaProducerMain Log");
        FileHandler fh;
        try{
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String inputDir = cmd.getOptionValue("input_dir");
            String logDir = cmd.getOptionValue("log_dir");
            String runMode = "factory";
            int numberOfEvents = 10000;
            if (inputDir != null){
                runMode = "directory";
            }

            String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
            String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
            String logFile = logDir + "/" + KafkaProducerMain.class.getSimpleName() + "_" + currentTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

            logger.info("Load kafka properties");
            Properties kafkaProperties = new Properties();
            InputStream inputStream = KafkaProducerMain.class.getClassLoader().getResourceAsStream("properties/kafka.properties");
            kafkaProperties.load(inputStream);
            String bootstrapServer = kafkaProperties.getProperty("bootstrap.server");
            String kafkaTopic = kafkaProperties.getProperty("kafka.topic");

            logger.info("Set kafka producer properties");
            Properties kafkaProducerProps = new Properties();
            kafkaProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            kafkaProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            kafkaProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomerEventSerializer.class);

            KafkaProducer<String, CustomerEvent> kafkaProducer = new KafkaProducer<>(kafkaProducerProps);

            List<CustomerEvent> customerEvents = new ArrayList<>();
            switch (runMode){
                case "directory":
                    logger.info("Read customer events from directory");
                    customerEvents = JsonUtils.readCustomerEventsFromDirectory(inputDir);
                    break;
                case "factory":
                    logger.info("Create random customer event sample data");
                    CustomerEventFactory customerEventFactory = new CustomerEventFactory(logger);
                    customerEvents =
                            customerEventFactory.createCustomerEventSampleData(0, numberOfEvents, false);
                    break;
            }

            logger.info("Send customer events to kafka topic: " + kafkaTopic);
            for(int i=0;i<customerEvents.size();i++){
                String key = "key-"+i;
                CustomerEvent value = customerEvents.get(i);
                RecordMetadata recordMetadata = kafkaProducer.send(new ProducerRecord<>(kafkaTopic, key, value)).get();
                // System.out.printf("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)%n", key, value, recordMetadata.partition(), recordMetadata.offset());
            }
        } catch (IOException e){
            logger.info(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            logger.info(e.getMessage());
        } catch (ParseException e) {
            logger.info(e.getMessage());
            formatter.printHelp("Kafka Producer Main", options);
        }

    }
}
