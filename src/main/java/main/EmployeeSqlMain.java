package main;

import company.*;
import utils.PostgreSqlUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Class with a main method and different run modes in a sql context (select, insert, create) to query the Employee table.
 */
public class EmployeeSqlMain {

    public static void main(String[] args){
        try{
            String database = "company";
            String inputTable = EmployeeTable.TABLE_NAME;
            int numberOfEmployeesToCreate = 1000;
            String runMode = "select";

            PostgreSqlUtils postgresSqlConnection = new PostgreSqlUtils(database);
            Connection connection = postgresSqlConnection.getPostgreSqlConnection();
            Statement statement = postgresSqlConnection.getSqlStatement(connection);

            EmployeeFactory empFactory = new EmployeeFactory();

            switch (runMode){
                case "select":
                    ResultSet selectAllResult = SqlStatements.selectAllFromTable(statement, inputTable);
                    List<Employee> employees = SqlStatements.createEmployeeListFromSqlResult(selectAllResult);
                    for(Employee employee : employees){
                        System.out.println(employee.toString());
                    }
                    selectAllResult.close();
                    break;
                case "truncate":
                    SqlStatements.truncateTable(connection, inputTable);
                    break;
                case "insert":
                    int maxId = SqlStatements.getMaxIdFromTable(statement, inputTable, EmployeeTable.ID_COLUMN);
                    if(maxId != -1){
                        List<Employee> randomEmployees =
                                empFactory.createEmployeeSampleData(maxId, numberOfEmployeesToCreate);
                        SqlStatements.insertEmployeesIntoTable(connection, inputTable, randomEmployees);
                    } else {
                        System.out.println("Creation of random employees failed because max id is " + maxId);
                    }
                    break;
                case "create":
                    SqlStatements.createTable(connection, inputTable);
            }

            statement.close();
            connection.close();
        } catch (SQLException e){
            System.out.println("PostgreSql main method failed");
            e.printStackTrace();
        }
    }
}
