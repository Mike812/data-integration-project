package company;

import utils.PostgreSqlUtils;
import utils.SqlStatementUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Interacts with a postgreSql database and consists of methods that represent common sql commands.
 * Provides functionality to insert randomly created sample data.
 */
public class SqlStatements {

    private Connection connection;
    private Logger logger;

    public SqlStatements(Connection connection, Logger logger){
        this.connection = connection;

        if(logger == null){
            this.logger = Logger.getLogger("Sql Statements Log");
        } else {
            this.logger = logger;
        }
    }

    public int createDatabase(String database){
        int result = -1;
        try{
            String sqlString = SqlStatementUtils.getCreateDatabaseCmd(database);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Database created successfully");
            preparedStatement.close();
        } catch (SQLException e) {
            logger.info("Create database failed");
            e.printStackTrace();
        }

        return result;
    }

    public int dropDatabase(String database){
        int result = -1;
        try{
            String sqlString = SqlStatementUtils.getDropDatabaseCmd(database);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Database dropped successfully if exists");
            preparedStatement.close();
        } catch (SQLException e) {
            logger.info("Drop database failed");
            e.printStackTrace();
        }

        return result;
    }

    public int createTable(String inputTable){
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
            if (!sqlString.isEmpty()){
                PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
                result = preparedStatement.executeUpdate();
                logger.info("Table created successfully if not exists");
                preparedStatement.close();
            } else {
                logger.info("Create table sql string could not be generated");
                throw new SQLException();
            }
        } catch (SQLException e) {
            logger.info("Create table failed.");
            e.printStackTrace();
        }

        return result;
    }

    public int dropTable(String inputTable){
        int result = -1;
        try{
            String sqlString = SqlStatementUtils.getDropTableCmd(inputTable);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Table dropped successfully if exists");
            preparedStatement.close();
        } catch (SQLException e) {
            logger.info("Drop table failed");
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet selectAllFromTable(String inputTable) {
        ResultSet resultSet = null;
        try {
            String selectAllFromTableString = SqlStatementUtils.getSelectAllFromTableCmd(inputTable);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(selectAllFromTableString);
            statement.close();
        } catch (SQLException e){
            logger.info("Get max id from sql result failed");
            e.printStackTrace();
        }

        return resultSet;
    }

    public int truncateTable(String inputTable) {
        int result = -1;
        try{
            String sqlString = SqlStatementUtils.getTruncateTableCmd(inputTable);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Values truncated from table " + inputTable);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.info("Truncate table failed");
            e.printStackTrace();
        }

        return result;
    }

    public int getMaxIdFromTable(String inputTable, String idField) {
        int maxId = -1;
        try {
            String maxIdFromTableString = SqlStatementUtils.getMaxIdFromTableCmd(inputTable, idField);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(maxIdFromTableString);
            while(result.next()){
                maxId = result.getInt(SqlStatementUtils.maxId);
            }
            result.close();
            statement.close();
        } catch (SQLException e){
            logger.info("Get max id from sql result failed");
            e.printStackTrace();
        }

        return maxId;
    }

    public int insertEmployeesIntoTable(String table, List<Employee> employees){
        int result = -1;
        try{
            String sqlString = EmployeeTable.getInsertIntoTableString(table, employees);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Values were inserted successfully into table " + table);
            preparedStatement.close();
        } catch (SQLException e) {
            logger.info("Insert into table failed");
            e.printStackTrace();
        }

        return result;
    }

    public int insertCustomerEventsIntoTable(String table, List<CustomerEvent> customerEvents){
        int result = -1;
        try{
            String sqlString = CustomerEventTable.getInsertIntoTableString(table, customerEvents);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            result = preparedStatement.executeUpdate();
            logger.info("Values were inserted successfully into table " + table);
        } catch (SQLException e) {
            logger.info("Insert into table failed");
            e.printStackTrace();
        }

        return result;
    }

    public List<Employee> createEmployeeListFromSqlResult(ResultSet result) {
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
            logger.info("Create list from sql result set failed");
            e.printStackTrace();
        }

        return employees;
    }

    public List<CustomerEvent> createCustomerEventListFromSqlResult(ResultSet result) {
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
            logger.info("Create list from sql result set failed");
            e.printStackTrace();
        }

        return customerEvents;
    }
}
