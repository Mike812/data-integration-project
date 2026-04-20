package main;

import company.*;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.PostgreSqlUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InsertFromDirectoryMainTest {

    String database = "company";
    int samplesFirstRun = 10000;
    int samplesSecondRun = 5000;
    String logDir = "C:/data-integration-project/logs/";
    String jsonDir = "C:/data-integration-project/sample_data/json/";

    @Test
    public void testASampleDataMainEmployee() throws InterruptedException {
        String outputDir = jsonDir + EmployeeTable.TABLE_NAME;
        File directory = new File(outputDir);
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files){
                if(file.getPath().contains(EmployeeTable.TABLE_NAME) && file.getPath().endsWith(".json")){
                    file.delete();
                }
            }
        }

        String[] args = {"-l", logDir, "-t", EmployeeTable.TABLE_NAME, "-o", outputDir, "-n", String.valueOf(samplesFirstRun)};
        SampleDataMain.main(args);
        Thread.sleep(2000);
        String[] args2 = {"-l", logDir, "-t", EmployeeTable.TABLE_NAME, "-o", outputDir, "-n", String.valueOf(samplesSecondRun)};
        SampleDataMain.main(args2);

        File[] jsonFilesAfter = directory.listFiles();
        Assert.assertNotNull(jsonFilesAfter);
        assertEquals(2, jsonFilesAfter.length);
    }

    @Test
    public void testBInsertFromDirectoryMainEmployee() throws SQLException {
        SqlStatements sqlStatements = SqlStatementsFactory.getSqlStatementsObject(database, null);

        sqlStatements.truncateTable(EmployeeTable.TABLE_NAME);

        String inputDir = jsonDir + EmployeeTable.TABLE_NAME;
        String[] args = {"-d", database, "-i", inputDir, "-l", logDir, "-t", EmployeeTable.TABLE_NAME};
        InsertFromDirectoryMain.main(args);

        ResultSet selectAllResult = sqlStatements.selectAllFromTable(EmployeeTable.TABLE_NAME);
        List<Employee> employees = sqlStatements.createEmployeeListFromSqlResult(selectAllResult);

        int expected = samplesFirstRun + samplesSecondRun;
        assertEquals(expected, employees.size());

        selectAllResult.close();
        sqlStatements.closeConnections();
    }

    @Test
    public void testCSampleDataMainCustomerEvent() throws InterruptedException {
        String outputDir = jsonDir + CustomerEventTable.TABLE_NAME;
        File directory = new File(outputDir);
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files){
                if(file.getPath().contains(CustomerEventTable.TABLE_NAME) && file.getPath().endsWith(".json")){
                    file.delete();
                }
            }
        }

        String[] args = {"-l", logDir, "-t", CustomerEventTable.TABLE_NAME, "-o", outputDir, "-n", String.valueOf(samplesFirstRun)};
        SampleDataMain.main(args);
        Thread.sleep(2000);
        String[] args2 = {"-l", logDir, "-t", CustomerEventTable.TABLE_NAME, "-o", outputDir, "-n", String.valueOf(samplesSecondRun)};
        SampleDataMain.main(args2);

        File[] jsonFilesAfter = directory.listFiles();
        Assert.assertNotNull(jsonFilesAfter);
        assertEquals(2, jsonFilesAfter.length);
    }

    @Test
    public void testDInsertFromDirectoryMainCustomerEvent() throws SQLException {
        SqlStatements sqlStatements = SqlStatementsFactory.getSqlStatementsObject(database, null);

        sqlStatements.truncateTable(CustomerEventTable.TABLE_NAME);

        String inputDir = jsonDir + CustomerEventTable.TABLE_NAME;
        String[] args = {"-d", database, "-i", inputDir, "-l", logDir, "-t", CustomerEventTable.TABLE_NAME};
        InsertFromDirectoryMain.main(args);

        ResultSet selectAllResult = sqlStatements.selectAllFromTable(CustomerEventTable.TABLE_NAME);
        List<CustomerEvent> customerEvents = sqlStatements.createCustomerEventListFromSqlResult(selectAllResult);

        int expected = samplesFirstRun + samplesSecondRun;
        assertEquals(expected, customerEvents.size());

        selectAllResult.close();
        sqlStatements.closeConnections();
    }
}
