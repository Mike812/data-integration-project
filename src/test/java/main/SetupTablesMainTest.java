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
        String logDir = "C:/data-integration-project/logs/";
        int numberOfEmployees = 100;
        int numberOfCustomerEvents = 1000;

        SqlStatements sqlStatements = SqlStatementsFactory.getSqlStatementsObject(database, null);

        String[] args = {"-d", database, "-l", logDir, "-e", String.valueOf(numberOfEmployees),
                "-c", String.valueOf(numberOfCustomerEvents)};
        SetupTablesMain.main(args);

        ResultSet selectAllResult = sqlStatements.selectAllFromTable(EmployeeTable.TABLE_NAME);
        List<Employee> employees = sqlStatements.createEmployeeListFromSqlResult(selectAllResult);
        assertEquals(numberOfEmployees, employees.size());

        ResultSet selectAllResult2 = sqlStatements.selectAllFromTable(CustomerEventTable.TABLE_NAME);
        List<CustomerEvent> customerEvents = sqlStatements.createCustomerEventListFromSqlResult(selectAllResult2);
        assertEquals(numberOfCustomerEvents, customerEvents.size());

        selectAllResult.close();
        selectAllResult2.close();
        sqlStatements.closeConnections();
    }
}
