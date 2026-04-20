package main;

import company.*;
import utils.CompanyDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class with a main method and different run modes in a sql context (select, insert, create) to query the Employee table.
 */
public class PostgreSqlMain {

    public static void main(String[] args){
        try{
            String inputTable = CustomerEventTable.TABLE_NAME;
            int numberOfSamplesToCreate = 1000;
            String runMode = "select";

            Connection databaseConnection = CompanyDatabaseConnection.getPostgresConnection();
            SqlStatements sqlStatements = new SqlStatements(databaseConnection, null);
            CustomerEventFactory customerEventFactory = new CustomerEventFactory(null);
            EmployeeFactory employeeFactory = new EmployeeFactory(null);

            switch (runMode){
                case "select":
                    ResultSet selectAllResult = sqlStatements.selectAllFromTable(inputTable);
                    if (inputTable.equals(EmployeeTable.TABLE_NAME)){
                        List<Employee> employees = employeeFactory.createEmployeeListFromSqlResult(selectAllResult);
                        for(Employee employee : employees){
                            System.out.println(employee.toString());
                        }
                        selectAllResult.close();
                    } else {
                        List<CustomerEvent> customerEvents = customerEventFactory.createCustomerEventListFromSqlResult(selectAllResult);
                        for(CustomerEvent customerEvent : customerEvents){
                            System.out.println(customerEvent.toString());
                        }
                        selectAllResult.close();
                    }
                    break;
                case "truncate":
                    sqlStatements.truncateTable(inputTable);
                    break;
                case "insert":
                    if(inputTable.equals(EmployeeTable.TABLE_NAME)){
                        int maxId = sqlStatements.getMaxIdFromTable(inputTable, EmployeeTable.ID_COLUMN);
                        List<Employee> randomEmployees =
                                employeeFactory.createEmployeeSampleData(maxId, numberOfSamplesToCreate, true);
                        sqlStatements.insertEmployeesIntoTable(inputTable, randomEmployees);
                    } else if(inputTable.equals(CustomerEventTable.TABLE_NAME)){
                        int maxId = sqlStatements.getMaxIdFromTable(inputTable, CustomerEventTable.ID_COLUMN);
                        List<CustomerEvent> randomCustomerEvens =
                                customerEventFactory.createCustomerEventSampleData(maxId, numberOfSamplesToCreate, true);
                        sqlStatements.insertCustomerEventsIntoTable(inputTable, randomCustomerEvens);
                    } else {
                        System.out.println("Insert failed");
                    }
                    break;
                case "create":
                    sqlStatements.createTable(inputTable);
                    break;
            }
        } catch (SQLException e){
            System.out.println("PostgreSql main method failed");
            e.printStackTrace();
        }
    }
}
