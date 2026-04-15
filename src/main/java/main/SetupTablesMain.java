package main;

import company.*;
import org.apache.commons.cli.*;
import utils.InputOutputUtils;
import utils.PostgreSqlUtils;

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

        Option databaseOption = new Option("d", "database", true, "input database");
        databaseOption.setRequired(true);
        options.addOption(databaseOption);

        Option employeeSamples = new Option("e", "employees", true,
                "number of employee samples");
        employeeSamples.setRequired(true);
        options.addOption(employeeSamples);

        Option customerSamples = new Option("c", "customers", true,
                "number of customer event samples");
        customerSamples.setRequired(true);
        options.addOption(customerSamples);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        Logger logger = Logger.getLogger("Setup Tables Main Log");
        FileHandler fh = null;

        try{
            String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
            String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
            String logFile = "C:/data-integration-project/logs/" + SetupTablesMain.class.getSimpleName() + "_" + currentTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

            CommandLine cmd = parser.parse(options, args);
            String database = cmd.getOptionValue("database");
            int numberOfEmployeeSamples = Integer.parseInt(cmd.getOptionValue("employees"));
            int numberOfCustomerSamples = Integer.parseInt(cmd.getOptionValue("customers"));

            PostgreSqlUtils postgresSqlConnection = new PostgreSqlUtils(database);
            Connection connection = postgresSqlConnection.getPostgreSqlConnection();
            Statement statement = postgresSqlConnection.getSqlStatement(connection);

            logger.info("Create tables");
            SqlStatements.createTable(connection, EmployeeTable.TABLE_NAME);
            SqlStatements.createTable(connection, CustomerEventTable.TABLE_NAME);

            logger.info("Truncate tables");
            SqlStatements.truncateTable(connection, EmployeeTable.TABLE_NAME);
            SqlStatements.truncateTable(connection, CustomerEventTable.TABLE_NAME);

            logger.info("Create random sample data and insert values in employee");
            EmployeeFactory empFactory = new EmployeeFactory();
            List<Employee> randomEmployees =
                    empFactory.createEmployeeSampleData(0, numberOfEmployeeSamples);
            SqlStatements.insertEmployeesIntoTable(connection, EmployeeTable.TABLE_NAME, randomEmployees);

            logger.info("Create random sample data and insert values in customer event");
            CustomerEventFactory customerEventFactory = new CustomerEventFactory();
            List<CustomerEvent> customerEvents =
                    customerEventFactory.createCustomerEventSampleData(0, numberOfCustomerSamples);
            SqlStatements.insertCustomerEventsIntoTable(connection, CustomerEventTable.TABLE_NAME,
                    customerEvents);

            statement.close();
            connection.close();
        } catch (SQLException e){
            logger.info("Sql exception in setup tables main method");
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (ParseException e){
            logger.info("Parser exception in setup tables main method");
            System.out.println(e.getMessage());
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
