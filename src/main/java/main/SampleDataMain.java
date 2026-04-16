package main;

import company.*;
import org.apache.commons.cli.*;
import utils.InputOutputUtils;
import utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class with a main method that creates sample data for a given database and table
 */
public class SampleDataMain {

    public static void main(String[] args){

        Options options = new Options();

        Option id = new Option("id", "start_id", true, "start number of a sequential id");
        id.setRequired(true);
        options.addOption(id);

        Option logDirOption = new Option("l", "log_dir", true, "directory for log files");
        logDirOption.setRequired(true);
        options.addOption(logDirOption);

        Option samples = new Option("n", "number_samples", true, "number of samples");
        samples.setRequired(true);
        options.addOption(samples);

        Option output = new Option("o", "output", true, "output path");
        output.setRequired(true);
        options.addOption(output);

        Option input = new Option("t", "table", true, "input table");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        Logger logger = Logger.getLogger("Sample Data Main Log");
        FileHandler fh = null;

        try {
            CommandLine cmd = parser.parse(options, args);
            String inputTable = cmd.getOptionValue("table");
            String logDir = cmd.getOptionValue("log_dir");
            String outputPath = cmd.getOptionValue("output");
            int numberOfSamples = Integer.parseInt(cmd.getOptionValue("number_samples"));
            int startId = Integer.parseInt(cmd.getOptionValue("start_id"));

            String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
            String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
            String logFile = logDir + "/" + SampleDataMain.class.getSimpleName() + "_" + currentTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

            switch (inputTable){
                case EmployeeTable.TABLE_NAME:
                    logger.info("Create random employee sample data");
                    EmployeeFactory empFactory = new EmployeeFactory();
                    List<Employee> employees = empFactory.createEmployeeSampleData(startId, numberOfSamples);
                    logger.info("Write employee sample data to json file");
                    String pathToEmployeeJson = outputPath + "/employees_" + currentTimestamp + ".json";
                    JsonUtils.writeEmployeesToJsonFile(employees, pathToEmployeeJson);
                    break;
                case CustomerEventTable.TABLE_NAME:
                    logger.info("Create random customer event sample data");
                    CustomerEventFactory customerEventFactory = new CustomerEventFactory();
                    List<CustomerEvent> customerEvents =
                            customerEventFactory.createCustomerEventSampleData(startId, numberOfSamples);
                    logger.info("Write customer event sample data to json file");
                    String pathToCustomerJson = outputPath + "/customer_events_" + currentTimestamp + ".json";
                    JsonUtils.writeCustomerEventsToJsonFile(customerEvents, pathToCustomerJson);
                    break;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Sample data main", options);

            System.exit(1);
        } catch (IOException e){
            logger.info("Problem with writing json files in setup tables main method");
            logger.info(e.getMessage());
        } finally {
            fh.close();
        }
    }
}
