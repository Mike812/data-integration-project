package company;

import org.junit.Test;
import utils.JsonUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmployeeTest {

    @Test
    public void testToString() {
        String pathToJson = "json/employees_12-04-2026_15-19-01.json";
        List<Employee> employees = JsonUtils.readEmployeesFromJsonFile(pathToJson);
        String expectedString = "Employee ID: 0, Name: Dylan Curtis, Department: Sales, State: NV, " +
                "Salary: 63840, Age: 46, Bonus: 10000";
        assertEquals(expectedString, employees.get(0).toString());
    }
}
