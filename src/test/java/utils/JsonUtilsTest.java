package utils;

import company.CustomerEvent;
import company.CustomerEventFactory;
import company.Employee;
import company.EmployeeFactory;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class JsonUtilsTest {

    @Test
    public void testReadEmployeesFromJsonFile() {
        String jsonFile = "json/employees_12-04-2026_15-19-01.json";
        List<Employee> employees = JsonUtils.readEmployeesFromJsonFile(jsonFile);
        assertEquals(10, employees.size());

        Employee employee = employees.get(0);
        //assertEquals(1, employee.getEmployeeId());
        assertEquals("Dylan Curtis", employee.getName());
        assertEquals("Sales", employee.getDepartment());
        assertEquals("NV", employee.getState());
        assertEquals(46, employee.getAge());
        assertEquals(63840, employee.getSalary());
        assertEquals(10000, employee.getBonus());
    }

    @Test
    public void testReadCustomerEventsFromJsonFile() {
        String jsonFile = "json/customer_events_12-04-2026_17-16-03.json";
        List<CustomerEvent> customerEvents = JsonUtils.readCustomerEventsFromJsonFile(jsonFile);
        assertEquals(10, customerEvents.size());

        CustomerEvent customerEvent = customerEvents.get(0);
        //assertEquals(0, customerEvent.getCustomerEventId());
        assertEquals("Seetrue Technologies", customerEvent.getCustomerName());
        assertEquals("Platform as a Service", customerEvent.getProductName());
        assertEquals(4, customerEvent.getSalesAmount());
        assertEquals("2025-04-12 17:16:03", customerEvent.getEventTimestamp());
    }

    @Test
    public void testWriteEmployeesToJsonFile() {
        String timestampFormat = "dd-MM-yyyy_HH-mm-ss"; //"dd-MM-yyyy"
        String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
        String pathToEmployeeJson = "src/main/resources/json/employees_" + currentTimestamp + ".json";

        Path path = Paths.get(pathToEmployeeJson);
        assertFalse(Files.exists(path));

        EmployeeFactory employeeFactory = new EmployeeFactory();
        List<Employee> employees = employeeFactory.createEmployeeSampleData(0, 10, false);
        JsonUtils.writeEmployeesToJsonFile(employees, pathToEmployeeJson);
        assertTrue(Files.exists(path));

        File fileToDelete = new File(pathToEmployeeJson);
        fileToDelete.delete();
        assertFalse(Files.exists(path));
    }

    @Test
    public void testWriteCustomerEventsToJsonFile(){
        String timestampFormat = "dd-MM-yyyy_HH-mm-ss"; //"dd-MM-yyyy"
        String currentTimestamp = InputOutputUtils.getCurrentTimestamp(timestampFormat);
        String pathToCustomerJson = "src/main/resources/json/customer_events_" + currentTimestamp + ".json";

        Path path = Paths.get(pathToCustomerJson);
        assertFalse(Files.exists(path));

        CustomerEventFactory customerEventFactory = new CustomerEventFactory();
        List<CustomerEvent> customerEvents =
                customerEventFactory.createCustomerEventSampleData(0, 10, false);
        JsonUtils.writeCustomerEventsToJsonFile(customerEvents, pathToCustomerJson);
        assertTrue(Files.exists(path));

        File fileToDelete = new File(pathToCustomerJson);
        fileToDelete.delete();
        assertFalse(Files.exists(path));
    }
}
