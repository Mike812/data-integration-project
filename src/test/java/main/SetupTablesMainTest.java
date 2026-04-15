package main;

import company.*;
import org.junit.Test;
import utils.PostgreSqlUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SetupTablesMainTest {

    @Test
    public void testSetupTablesMain() throws SQLException, InterruptedException {
        String database = "company";
        int numberOfEmployees = 1000;
        int numberOfCustomerEvents = 10000;

        PostgreSqlUtils postgreSqlConnection = new PostgreSqlUtils(database);
        Connection connection = postgreSqlConnection.getPostgreSqlConnection();
        Statement statement = postgreSqlConnection.getSqlStatement(connection);

        String[] args = {"-d", database, "-e", String.valueOf(numberOfEmployees), "-c", String.valueOf(numberOfCustomerEvents)};
        SetupTablesMain.main(args);

        ResultSet selectAllResult = SqlStatements.selectAllFromTable(statement, EmployeeTable.TABLE_NAME);
        List<Employee> employees = SqlStatements.createEmployeeListFromSqlResult(selectAllResult);
        assertEquals(numberOfEmployees, employees.size());

        ResultSet selectAllResult2 = SqlStatements.selectAllFromTable(statement, CustomerEventTable.TABLE_NAME);
        List<CustomerEvent> customerEvents = SqlStatements.createCustomerEventListFromSqlResult(selectAllResult2);
        assertEquals(numberOfCustomerEvents, customerEvents.size());

        selectAllResult.close();
        selectAllResult2.close();
        statement.close();
        connection.close();
    }
}
