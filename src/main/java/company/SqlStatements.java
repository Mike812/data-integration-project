package company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interacts with a postgreSql database and consists of methods that represent common sql commands.
 * Provides functionality to insert randomly created sample data.
 */
public class SqlStatements {

    public static String maxId = "max_id";

    public static int createDatabase(Connection connection, String database){
        int result = -1;
        try{
            String sqlString = "CREATE DATABASE " + database;
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            System.out.println("Create database failed");
            e.printStackTrace();
        }

        return result;
    }

    public static int dropDatabase(Connection connection, String database){
        int result = -1;
        try{
            String sqlString = "DROP DATABASE IF EXISTS " + database;
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Database dropped successfully if exists");
        } catch (SQLException e) {
            System.out.println("Drop database failed");
            e.printStackTrace();
        }

        return result;
    }

    public static int createTable(Connection connection, String inputTable){
        int result = -1;
        String sqlString = "";

        switch (inputTable){
            case EmployeeTable.TABLE_NAME:
                sqlString = EmployeeTable.getCreateTableString();
                break;
            case CustomerEventTable.TABLE_NAME:
                sqlString = CustomerEventTable.getCreateTableString();
                break;
        }
        try{
            if (!sqlString.equals("")){
                PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
                result = preparedStatement.executeUpdate();
                System.out.println("Table created successfully if not exists");
            } else {
                System.out.println("Create table sql string could not be generated");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("Create table failed.");
            e.printStackTrace();
        }

        return result;
    }

    public static int dropTable(Connection connection, String inputTable){
        int result = -1;
        try{
            String sqlString = "DROP TABLE IF EXISTS " + inputTable;
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Table dropped successfully if exists");
        } catch (SQLException e) {
            System.out.println("Drop table failed");
            e.printStackTrace();
        }

        return result;
    }

    public static ResultSet selectAllFromTable(Statement statement, String inputTable) {
        ResultSet resultSet = null;
        try {
            String selectAllFromTableString = getSelectAllFromTableString(inputTable);
            resultSet = statement.executeQuery(selectAllFromTableString);
        } catch (SQLException e){
            System.out.println("Get max id from sql result failed");
            e.printStackTrace();
        }

        return resultSet;
    }

    public static int truncateTable(Connection connection, String inputTable) {
        int result = -1;
        try{
            String sqlString = getTruncateTableString(inputTable);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Values truncated from table " + inputTable);
        } catch (SQLException e) {
            System.out.println("Truncate table failed");
            e.printStackTrace();
        }

        return result;
    }

    public static int getMaxIdFromTable(Statement statement, String inputTable, String idField) throws SQLException {
        int maxId = -1;
        String maxIdFromTableString = getMaxIdFromTableString(inputTable, idField);
        ResultSet result = statement.executeQuery(maxIdFromTableString);

        try {
            while(result.next()){
                maxId = result.getInt(SqlStatements.maxId);
            }
            result.close();

        } catch (SQLException e){
            System.out.println("Get max id from sql result failed");
            e.printStackTrace();
        }

        return maxId;
    }

    public static int insertEmployeesIntoTable(Connection connection, String table, List<Employee> employees){
        int result = -1;
        try{
            String sqlString = EmployeeTable.getInsertIntoTableString(table, employees);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Values were inserted successfully into table " + table);
        } catch (SQLException e) {
            System.out.println("Insert into table failed");
            e.printStackTrace();
        }

        return result;
    }

    public static int insertCustomerEventsIntoTable(Connection connection, String table,
                                                    List<CustomerEvent> customerEvents){
        int result = -1;
        try{
            String sqlString = CustomerEventTable.getInsertIntoTableString(table, customerEvents);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            System.out.println("Values were inserted successfully into table " + table);
        } catch (SQLException e) {
            System.out.println("Insert into table failed");
            e.printStackTrace();
        }

        return result;
    }

    public static int insertCustomerEventsIntoTable(Connection connection, Statement statement, List<CustomerEvent> customerEvents){
        int result = -1;
        try {
            int maxId = SqlStatements.getMaxIdFromTable(statement, CustomerEventTable.TABLE_NAME, CustomerEventTable.ID_COLUMN);
            List<CustomerEvent> customerEventsWithId = CustomerEventFactory.addIdToCustomerEvents(customerEvents, maxId);
            result = SqlStatements.insertCustomerEventsIntoTable(connection, CustomerEventTable.TABLE_NAME, customerEventsWithId);
            System.out.println("Values were inserted successfully into customer events table");
        } catch (SQLException e){
            System.out.println("Insert into table failed");
            e.printStackTrace();
        }

        return result;
    }

    public static List<Employee> createEmployeeListFromSqlResult(ResultSet result) {
        List<Employee> employees = new ArrayList<>();
        try {
            while(result.next()){
                employees.add(
                        new Employee(result.getInt(EmployeeTable.ID_COLUMN),
                                result.getString(EmployeeTable.NAME_COLUMN),
                                result.getString(EmployeeTable.DEPARTMENT_COLUMN),
                                result.getString(EmployeeTable.STATE_COLUMN),
                                result.getInt(EmployeeTable.SALARY_COLUMN),
                                result.getInt(EmployeeTable.AGE_COLUMN),
                                result.getInt(EmployeeTable.BONUS_COLUMN)));
            }
        } catch (SQLException e) {
            System.out.println("Create list from sql result set failed");
            e.printStackTrace();
        }

        return employees;
    }

    public static List<CustomerEvent> createCustomerEventListFromSqlResult(ResultSet result) {
        List<CustomerEvent> customerEvents = new ArrayList<>();
        try {
            while(result.next()){
                customerEvents.add(
                        new CustomerEvent(result.getInt(CustomerEventTable.ID_COLUMN),
                                result.getString(CustomerEventTable.CUSTOMER_NAME_COLUMN),
                                result.getString(CustomerEventTable.PRODUCT_NAME_COLUMN),
                                result.getInt(CustomerEventTable.SALES_AMOUNT_COLUMN),
                                result.getString(CustomerEventTable.TIMESTAMP_COLUMN)));
            }
        } catch (SQLException e) {
            System.out.println("Create list from sql result set failed");
            e.printStackTrace();
        }

        return customerEvents;
    }

    public static String getSelectAllFromTableString(String table){
        return "SELECT * FROM " + table;
    }

    public static String getTruncateTableString(String table){
        return "TRUNCATE " + table;
    }

    public static String getMaxIdFromTableString(String table, String idField){
        return "SELECT max(\"" + idField + "\") as " + maxId + " FROM " + table;
    }
}
