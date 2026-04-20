package main;

import company.*;
import org.apache.commons.cli.*;
import utils.CompanyDatabaseConnection;
import utils.InputOutputUtils;
import utils.PostgreSqlUtils;
import utils.TestDatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class with a main method that creates tables in an existing database and inserts random sample data.
 */
public class SetupTablesMain {

    public static void main(String[] args) throws InterruptedException{

        Options options = new Options();

        Option customerOption = new Option("c", "customer_events", true,
                "number of customer event samples");
        options.addOption(customerOption);

        Option databaseOption = new Option("d", "database", true, "input database");
        databaseOption.setRequired(true);
        options.addOption(databaseOption);

        Option employeeOption = new Option("e", "employees", true,
                "number of employee samples");
        options.addOption(employeeOption);

        Option logDirOption = new Option("l", "log_dir", true, "directory for log files");
        logDirOption.setRequired(true);
        options.addOption(logDirOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        Logger logger = Logger.getLogger("Setup Tables Main Log");
        FileHandler fh = null;

        try{
            CommandLine cmd = parser.parse(options, args);
            String database = cmd.getOptionValue("database");
            String logDir = cmd.getOptionValue("log_dir");
            int numberOfEmployeeSamples = 0;
            int numberOfCustomerEventSamples = 0;
            if(cmd.getOptionValue("employees") != null){
                numberOfEmployeeSamples = Integer.parseInt(cmd.getOptionValue("employees"));
            }
            if(cmd.getOptionValue("customer_events") != null){
                numberOfCustomerEventSamples = Integer.parseInt(cmd.getOptionValue("customer_events"));
            }

            String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
            String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
            String logFile = logDir + "/" + SetupTablesMain.class.getSimpleName() + "_" + currentTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

            Connection databaseConnection;
            if(database.equals(CompanyDatabaseConnection.getDatabaseName())){
                databaseConnection = CompanyDatabaseConnection.getPostgresConnection();
            } else {
                databaseConnection = TestDatabaseConnection.getPostgresConnection();
            }

            SqlStatements sqlStatements = new SqlStatements(databaseConnection, logger);

            logger.info("Create tables");
            sqlStatements.createTable(EmployeeTable.TABLE_NAME);
            sqlStatements.createTable(CustomerEventTable.TABLE_NAME);

            logger.info("Truncate tables");
            sqlStatements.truncateTable(EmployeeTable.TABLE_NAME);
            sqlStatements.truncateTable(CustomerEventTable.TABLE_NAME);

            if(numberOfEmployeeSamples>0){
                logger.info("Create random sample data and insert values in employee");
                EmployeeFactory empFactory = new EmployeeFactory(logger);
                List<Employee> randomEmployees =
                        empFactory.createEmployeeSampleData(0, numberOfEmployeeSamples, true);
                sqlStatements.insertEmployeesIntoTable(EmployeeTable.TABLE_NAME, randomEmployees);
            }

            if(numberOfCustomerEventSamples>0){
                logger.info("Create random sample data and insert values in customer event");
                CustomerEventFactory customerEventFactory = new CustomerEventFactory(logger);
                List<CustomerEvent> customerEvents =
                        customerEventFactory.createCustomerEventSampleData(0, numberOfCustomerEventSamples, true);
                sqlStatements.insertCustomerEventsIntoTable(CustomerEventTable.TABLE_NAME,
                        customerEvents);
            }

        } catch (ParseException e){
            logger.info("Parser exception in setup tables main method");
            logger.info(e.getMessage());
            formatter.printHelp("Setup tables main", options);

            System.exit(1);
        } catch (IOException e) {
            logger.info("Problem with log file in setup tables main method");
            logger.info(e.getMessage());
            e.printStackTrace();
        } finally {
            fh.close();
        }
    }
}
