package main;

import company.*;
import org.junit.Test;
import utils.PostgreSqlUtils;
import utils.TestDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SetupTablesMainTest {

    String database = TestDatabaseConnection.getDatabaseName();
    Connection databaseConnection = TestDatabaseConnection.getPostgresConnection();
    EmployeeFactory employeeFactory = new EmployeeFactory(null);
    CustomerEventFactory customerEventFactory = new CustomerEventFactory(null);

    @Test
    public void testSetupTablesMain() throws SQLException, InterruptedException {
        String logDir = "C:/data-integration-project/logs/";
        int numberOfEmployees = 100;
        int numberOfCustomerEvents = 1000;

        SqlStatements sqlStatements = new SqlStatements(databaseConnection, null);

        String[] args = {"-d", database, "-l", logDir, "-e", String.valueOf(numberOfEmployees),
                "-c", String.valueOf(numberOfCustomerEvents)};
        SetupTablesMain.main(args);

        ResultSet selectAllResult = sqlStatements.selectAllFromTable(EmployeeTable.TABLE_NAME);
        List<Employee> employees = employeeFactory.createEmployeeListFromSqlResult(selectAllResult);
        assertEquals(numberOfEmployees, employees.size());

        ResultSet selectAllResult2 = sqlStatements.selectAllFromTable(CustomerEventTable.TABLE_NAME);
        List<CustomerEvent> customerEvents = customerEventFactory.createCustomerEventListFromSqlResult(selectAllResult2);
        assertEquals(numberOfCustomerEvents, customerEvents.size());

        selectAllResult.close();
        selectAllResult2.close();
    }
}
