package main;

import company.*;
import org.apache.commons.cli.*;
import utils.InputOutputUtils;
import utils.JsonUtils;
import utils.PostgreSqlUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class InsertFromDirectoryMain {

    public static void main(String[] args){
        Options options = new Options();

        Option databaseOption = new Option("d", "database", true, "input database");
        databaseOption.setRequired(true);
        options.addOption(databaseOption);

        Option inputDirOption = new Option("i", "input_dir", true,
                "input directory with json files");
        inputDirOption.setRequired(true);
        options.addOption(inputDirOption);

        Option tableOption = new Option("t", "table", true,
                "table in which values are inserted (employee or customer_event)");
        tableOption.setRequired(true);
        options.addOption(tableOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        Logger logger = Logger.getLogger("Insert From Directory Main Log");
        FileHandler fh = null;

        try{
            String timestampFormat = "dd-MM-yyyy_HH-mm-ss";
            String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
            String logFile = "C:/data-integration-project/logs/" + InsertFromDirectoryMain.class.getSimpleName() + "_" + currentTimestamp + ".log";
            fh = new FileHandler(logFile, true);
            logger.addHandler(fh);

            CommandLine cmd = parser.parse(options, args);
            String database = cmd.getOptionValue("database");
            String inputDir = cmd.getOptionValue("input_dir");
            String table = cmd.getOptionValue("table");

            PostgreSqlUtils postgresSqlConnection = new PostgreSqlUtils(database);
            Connection connection = postgresSqlConnection.getPostgreSqlConnection();
            Statement statement = postgresSqlConnection.getSqlStatement(connection);

            int limit = 10000;
            if (table.equals(CustomerEventTable.TABLE_NAME)){
                logger.info("Read customer events from directory");
                List<CustomerEvent> allCustomerEvents = JsonUtils.readCustomerEventsFromDirectory(inputDir);
                logger.info("Truncate customer event table");
                SqlStatements.truncateTable(connection, CustomerEventTable.TABLE_NAME);
                if (allCustomerEvents.size() > limit){
                    logger.info("Insert customer events in sublists");
                    insertCustomerEventSublistsIntoTable(connection, allCustomerEvents, limit);
                } else {
                    logger.info("Insert customer events at once");
                    SqlStatements.insertCustomerEventsIntoTable(connection, CustomerEventTable.TABLE_NAME, allCustomerEvents);
                }
            }
            else if (table.equals(EmployeeTable.TABLE_NAME)){
                logger.info("Read employees from directory");
                List<Employee> allEmployees = JsonUtils.readEmployeesFromDirectory(inputDir);
                logger.info("Truncate employee table");
                SqlStatements.truncateTable(connection, EmployeeTable.TABLE_NAME);
                if (allEmployees.size() > limit){
                    logger.info("Insert employees in sublists");
                    insertEmployeeSublistsIntoTable(connection, allEmployees, limit);
                } else {
                    logger.info("Insert employees at once");
                    SqlStatements.insertEmployeesIntoTable(connection, EmployeeTable.TABLE_NAME, allEmployees);
                }
            }
            else {
                System.out.println("Table name " + table + " is not supported. Use " + EmployeeTable.TABLE_NAME +
                        " or " + CustomerEventTable.TABLE_NAME + " instead");
            }

            statement.close();
            connection.close();
        } catch (SQLException e){
            logger.info("Sql problem in main method");
            logger.info(e.getMessage());
        } catch (ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("Setup tables main", options);
        } catch (IOException e) {
            logger.info("Input output problem in main method");
            logger.info(e.getMessage());
        } finally {
            fh.close();
        }
    }

    public static void insertCustomerEventSublistsIntoTable(Connection connection, List<CustomerEvent> allCustomerEvents,
                                                            int limit){
        int size = allCustomerEvents.size();
        int right = limit;
        if (allCustomerEvents.size() > limit){
            for(int i=0;i<size;i+=limit){
                if(right>size){
                    right = size;
                }
                List<CustomerEvent> customerEvents = allCustomerEvents.subList(i, right);
                SqlStatements.insertCustomerEventsIntoTable(connection, CustomerEventTable.TABLE_NAME, customerEvents);
                System.out.println("Successfully inserted values into table " + CustomerEventTable.TABLE_NAME);
                right += limit;
            }
        }
    }

    public static void insertEmployeeSublistsIntoTable(Connection connection, List<Employee> allEmployees, int limit){
        int size = allEmployees.size();
        int right = limit;
        if (allEmployees.size() > limit){
            for(int i=0;i<size;i+=limit){
                if(right>size){
                    right = size;
                }
                List<Employee> employees = allEmployees.subList(i, right);
                SqlStatements.insertEmployeesIntoTable(connection, EmployeeTable.TABLE_NAME, employees);
                System.out.println("Successfully inserted values into table " + EmployeeTable.TABLE_NAME);
                right += limit;
            }
        }
    }
}
