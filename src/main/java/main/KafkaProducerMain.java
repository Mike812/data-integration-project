package main;

import company.*;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.CustomerEventSerializer;
import utils.JsonUtils;
import utils.PostgreSqlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducerMain {

    public static void main(String[] args){

        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        Option databaseOption = new Option("d", "database", true, "input database");
        databaseOption.setRequired(true);
        options.addOption(databaseOption);

        Option inputDirOption = new Option("i", "input_dir", true, "input directory with json files");
        options.addOption(inputDirOption);

        try{
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String database = cmd.getOptionValue("database");
            String inputDir = cmd.getOptionValue("input_dir");
            String runMode = "factory";
            int numberOfEvents = 10000;
            if (inputDir != null){
                runMode = "directory";
            }

            SqlStatements sqlStatements = SqlStatementsFactory.getSqlStatementsObject(database, null);

            Properties kafkaProperties = new Properties();
            InputStream inputStream = KafkaProducerMain.class.getClassLoader().getResourceAsStream("properties/kafka.properties");
            kafkaProperties.load(inputStream);
            String bootstrapServer = kafkaProperties.getProperty("bootstrap.server");
            String kafkaTopic = kafkaProperties.getProperty("kafka.topic");

            Properties kafkaProducerProps = new Properties();
            kafkaProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            kafkaProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            kafkaProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomerEventSerializer.class);

            KafkaProducer<String, CustomerEvent> kafkaProducer = new KafkaProducer<>(kafkaProducerProps);

            List<CustomerEvent> customerEvents = new ArrayList<>();
            switch (runMode){
                case "database":
                    ResultSet selectAllResult = sqlStatements.selectAllFromTable(CustomerEventTable.TABLE_NAME);
                    customerEvents = sqlStatements.createCustomerEventListFromSqlResult(selectAllResult);
                    break;
                case "directory":
                    customerEvents = JsonUtils.readCustomerEventsFromDirectory(inputDir);
                    break;
                case "factory":
                    CustomerEventFactory customerEventFactory = new CustomerEventFactory();
                    customerEvents =
                            customerEventFactory.createCustomerEventSampleData(0, numberOfEvents, false);
                    break;
            }

            for(int i=0;i<customerEvents.size();i++){
                String key = "key-"+i;
                CustomerEvent value = customerEvents.get(i);
                RecordMetadata recordMetadata = kafkaProducer.send(new ProducerRecord<>(kafkaTopic, key, value)).get();
                // System.out.printf("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)%n", key, value, recordMetadata.partition(), recordMetadata.offset());
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Kafka Customer Events Main", options);
        }

    }
}
