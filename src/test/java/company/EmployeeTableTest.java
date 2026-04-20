package company;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static utils.JsonUtils.readEmployeesFromJsonFile;

public class EmployeeTableTest {

    EmployeeFactory employeeFactory = new EmployeeFactory(null);

    @Test
    public void testGetCreateTableString(){
        String expectedString = "CREATE TABLE IF NOT EXISTS employee (employee_id int PRIMARY KEY, " +
                "name VARCHAR(50),department VARCHAR(50),state VARCHAR(2),salary INT,age INT,bonus INT);";

        assertEquals(expectedString, EmployeeTable.getCreateTableString());
    }

    @Test
    public void testInsertIntoTableString() {
        String pathToEmployeeJson = "json/employees_12-04-2026_15-19-01.json";
        List<Employee> employeesJson = readEmployeesFromJsonFile(pathToEmployeeJson);
        List<Employee> employeesJsonWithId = employeeFactory.addIdToEmployees(employeesJson, 0);
        String expectedString = "INSERT INTO employee VALUES  (1, 'Dylan Curtis', 'Sales', 'NV', 63840, 46, 10000), " +
                "(2, 'Michelle Rogers', 'Marketing', 'NV', 66165, 55, 5000), " +
                "(3, 'Catherine Bernard', 'Accounting', 'NV', 73457, 33, 1000), " +
                "(4, 'Doris Romero', 'Sales', 'DE', 60472, 27, 5000), " +
                "(5, 'Keith Bentley', 'Accounting', 'AR', 71168, 49, 2000), " +
                "(6, 'Keith Rios', 'Finance', 'KY', 50045, 36, 2000), " +
                "(7, 'Raymond Monroe', 'Marketing', 'MN', 84018, 56, 2000), " +
                "(8, 'Abigail Horn', 'IT', 'NY', 90392, 48, 1000), " +
                "(9, 'Christopher Horn', 'Accounting', 'TX', 59088, 54, 15000), " +
                "(10, 'Jack Greene', 'IT', 'MS', 89772, 27, 1000);";

        String result = EmployeeTable.getInsertIntoTableString(EmployeeTable.TABLE_NAME, employeesJsonWithId);

        assertEquals(expectedString, result);
    }
}
