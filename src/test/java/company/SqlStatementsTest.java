package company;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runners.MethodSorters;
import utils.PostgreSqlUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static utils.JsonUtils.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SqlStatementsTest {

    String database = "company";
    String testDatabase = "test_company";

    String employeeTable = EmployeeTable.TABLE_NAME;
    EmployeeFactory employeeFactory = new EmployeeFactory();
    int numberOfEmployees = 1000;
    int numberOfEmployeesJson = 10;
    List<Employee> employees = employeeFactory.createEmployeeSampleData(numberOfEmployeesJson, numberOfEmployees);
    String pathToEmployeeJson = "json/employees_12-04-2026_15-19-01.json";
    List<Employee> employeesJson = readEmployeesFromJsonFile(pathToEmployeeJson);

    String customerEventTable = CustomerEventTable.TABLE_NAME;
    CustomerEventFactory customerEventFactory = new CustomerEventFactory();
    int numberOfEvents = 1000;
    int numberOfEventsJson = 10;
    List<CustomerEvent> customerEvents =
            this.customerEventFactory.createCustomerEventSampleData(numberOfEventsJson, numberOfEvents);
    String pathToCustomerEventJson = "json/customer_events_12-04-2026_17-16-03.json";
    List<CustomerEvent> customerEventsJson = readCustomerEventsFromJsonFile(pathToCustomerEventJson);

    public SqlStatementsTest() throws IOException {}


    @Test
    @Order(1)
    public void testACreateDatabase() throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(database);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();

        int createDatabaseResult = SqlStatements.createDatabase(connection, testDatabase);
        assertEquals(0, createDatabaseResult);

        connection.close();
    }

    @Test
    @Order(2)
    public void testBCreateAndDropTable() throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(testDatabase);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();

        int createTableResult = SqlStatements.createTable(connection, employeeTable);
        assertEquals(0, createTableResult);

        int dropTableResult = SqlStatements.dropTable(connection, employeeTable);
        assertEquals(0, dropTableResult);

        int createCustomerEventResult = SqlStatements.createTable(connection, customerEventTable);
        assertEquals(0, createCustomerEventResult);

        int dropCustomerEventResult = SqlStatements.dropTable(connection, customerEventTable);
        assertEquals(0, dropCustomerEventResult);

        connection.close();
    }

    @Test
    @Order(3)
    public void testCInsertSelectAndMaxIdEmployee() throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(testDatabase);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();
        Statement statement = postgreSqlConnection.getSqlStatement(connection);


        int dropTableResult = SqlStatements.dropTable(connection, employeeTable);
        assertEquals(0, dropTableResult);

        int createTableResult = SqlStatements.createTable(connection, employeeTable);
        assertEquals(0, createTableResult);

        int insertJsonDataResult = SqlStatements.insertEmployeesIntoTable(connection, employeeTable, employeesJson);
        assertEquals(numberOfEmployeesJson, insertJsonDataResult);

        ResultSet selectAllResult = SqlStatements.selectAllFromTable(statement, employeeTable);
        while(selectAllResult.next()){
            assertEquals(1, selectAllResult.getInt(EmployeeTable.ID_COLUMN));
            assertEquals("Dylan Curtis", selectAllResult.getString(EmployeeTable.NAME_COLUMN));
            assertEquals("Sales", selectAllResult.getString(EmployeeTable.DEPARTMENT_COLUMN));
            assertEquals("NV", selectAllResult.getString(EmployeeTable.STATE_COLUMN));
            assertEquals(46, selectAllResult.getInt(EmployeeTable.AGE_COLUMN));
            assertEquals(63840, selectAllResult.getInt(EmployeeTable.SALARY_COLUMN));
            assertEquals(10000, selectAllResult.getInt(EmployeeTable.BONUS_COLUMN));
            break;
        }

        int maxId = SqlStatements.getMaxIdFromTable(statement, employeeTable, EmployeeTable.ID_COLUMN);
        assertEquals(numberOfEmployeesJson, maxId);

        int insertSampleDataResult = SqlStatements.insertEmployeesIntoTable(connection, employeeTable, employees);
        assertEquals(numberOfEmployees, insertSampleDataResult);

        connection.close();
        statement.close();
    }

    @Test
    @Order(4)
    public void testDInsertSelectAndMaxIdCustomerEvent() throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(testDatabase);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();
        Statement statement = postgreSqlConnection.getSqlStatement(connection);

        int dropTableResult = SqlStatements.dropTable(connection, customerEventTable);
        assertEquals(0, dropTableResult);

        int createTableResult = SqlStatements.createTable(connection, customerEventTable);
        assertEquals(0, createTableResult);

        int insertJsonDataResult = SqlStatements.insertCustomerEventsIntoTable(connection, customerEventTable,
                customerEventsJson);
        assertEquals(numberOfEventsJson, insertJsonDataResult);

        ResultSet selectAllResult = SqlStatements.selectAllFromTable(statement, customerEventTable);
        while(selectAllResult.next()){
            assertEquals(1, selectAllResult.getInt(CustomerEventTable.ID_COLUMN));
            assertEquals("Seetrue Technologies", selectAllResult.getString(CustomerEventTable.CUSTOMER_NAME_COLUMN));
            assertEquals("Platform as a Service", selectAllResult.getString(CustomerEventTable.PRODUCT_NAME_COLUMN));
            assertEquals(4, selectAllResult.getInt(CustomerEventTable.SALES_AMOUNT_COLUMN));
            assertEquals("2025-04-12 17:16:03", selectAllResult.getString(CustomerEventTable.TIMESTAMP_COLUMN));
            break;
        }

        int maxId = SqlStatements.getMaxIdFromTable(statement, customerEventTable, CustomerEventTable.ID_COLUMN);
        assertEquals(numberOfEventsJson, maxId);

        int insertTableResult = SqlStatements.insertCustomerEventsIntoTable(connection, customerEventTable,
                customerEvents);
        assertEquals(numberOfEvents, insertTableResult);

        connection.close();
        statement.close();
    }

    @Test
    @Order(5)
    public void testEDropDatabase() throws SQLException {
        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(database);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();

        int dropDatabaseResult = SqlStatements.dropDatabase(connection, testDatabase);
        assertEquals(0, dropDatabaseResult);

        connection.close();
    }
}
