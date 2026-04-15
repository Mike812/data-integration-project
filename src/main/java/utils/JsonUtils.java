package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import company.CustomerEvent;
import company.CustomerEventTable;
import company.Employee;
import company.EmployeeTable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CLass with methods to read and write json files
 */
public class JsonUtils {

    static String employeeRootName = "Employees";
    static String customerEventRootName = "Customer events";

    public static List<Employee> readEmployeesFromJsonFile(String pathToJson) {
        List<Employee> employees = new ArrayList<>();
        try {
            InputStream inputStream = InputOutputUtils.class.getClassLoader().getResourceAsStream(pathToJson);
            if(inputStream == null){
                System.out.println("Input stream is null when reading employees from json file");
                throw new IOException();
            }
            employees = readEmployees(inputStream);
        }
        catch (IOException e){
            System.out.println("Employees could not be read from json file");
            e.printStackTrace();
        }

        return employees;
    }

    public static List<Employee> readEmployees(InputStream inputStream) {
        Map<String, List<Employee>> employeesMap = new HashMap<>();
        try {
            employeesMap = new ObjectMapper().readValue(inputStream, new TypeReference<Map<String, List<Employee>>>() {});
        }
        catch (IOException e){
            System.out.println("Employees could not be read from json file");
            e.printStackTrace();
        }

        return employeesMap.get(employeeRootName);
    }

    public static List<Employee> readEmployees(File jsonFile) {
        Map<String, List<Employee>> employeesMap = new HashMap<>();
        try {
            employeesMap = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, List<Employee>>>() {});
        }
        catch (IOException e){
            System.out.println("Employees could not be read from json file");
            e.printStackTrace();
        }

        return employeesMap.get(employeeRootName);
    }

    public static List<CustomerEvent> readCustomerEventsFromJsonFile(String pathToJson) {
        List<CustomerEvent> customerEvents = new ArrayList<>();
        try {
            InputStream inputStream = InputOutputUtils.class.getClassLoader().getResourceAsStream(pathToJson);
            if(inputStream == null){
                System.out.println("Input stream is null when reading customer events from json file");
                throw new IOException();
            }
            customerEvents = readCustomerEvents(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customerEvents;
    }

    public static List<CustomerEvent> readCustomerEvents(InputStream inputStream) {
        Map<String, List<CustomerEvent>> customerEventsMap = new HashMap<>();
        try {
            customerEventsMap = new ObjectMapper().readValue(inputStream, new TypeReference<Map<String, List<CustomerEvent>>>() {});
        }
        catch (IOException e){
            System.out.println("Employees could not be read from json file");
            e.printStackTrace();
        }

        return customerEventsMap.get(customerEventRootName);
    }

    public static List<CustomerEvent> readCustomerEvents(File jsonFile) {
        Map<String, List<CustomerEvent>> customerEventsMap = new HashMap<>();
        try {
            customerEventsMap = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, List<CustomerEvent>>>() {});
        }
        catch (IOException e){
            System.out.println("Employees could not be read from json file");
            e.printStackTrace();
        }

        return customerEventsMap.get(customerEventRootName);
    }

    public static void writeEmployeesToJsonFile(List<Employee> employees, String pathToJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File outputFile = new File(pathToJson);

            ObjectNode root = mapper.createObjectNode();
            ArrayNode employeesNode = mapper.createArrayNode();

            for(Employee employee : employees){
                employeesNode.add(mapper.createObjectNode()
                        .put(EmployeeTable.ID_COLUMN, employee.getEmployeeId())
                        .put(EmployeeTable.NAME_COLUMN, employee.getName())
                        .put(EmployeeTable.DEPARTMENT_COLUMN, employee.getDepartment())
                        .put(EmployeeTable.STATE_COLUMN, employee.getState())
                        .put(EmployeeTable.AGE_COLUMN, employee.getAge())
                        .put(EmployeeTable.SALARY_COLUMN, employee.getSalary())
                        .put(EmployeeTable.BONUS_COLUMN, employee.getBonus()));
            }
            // Attach array to root object
            root.set(employeeRootName, employeesNode);
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
            System.out.println("Creation of employee sample data was successful");

        } catch (IOException e){
            System.out.println("Employees could not be written to json file");
            e.printStackTrace();
        }
    }

    public static void writeCustomerEventsToJsonFile(List<CustomerEvent> customerEvents, String pathToJson){
        try {
            ObjectMapper mapper = new ObjectMapper();
            File outputFile = new File(pathToJson);

            ObjectNode root = mapper.createObjectNode();
            ArrayNode customerEventNode = mapper.createArrayNode();

            for(CustomerEvent customerEvent : customerEvents){
                customerEventNode.add(mapper.createObjectNode()
                        .put(CustomerEventTable.ID_COLUMN, customerEvent.getCustomerEventId())
                        .put(CustomerEventTable.CUSTOMER_NAME_COLUMN, customerEvent.getCustomerName())
                        .put(CustomerEventTable.PRODUCT_NAME_COLUMN, customerEvent.getProductName())
                        .put(CustomerEventTable.SALES_AMOUNT_COLUMN, customerEvent.getSalesAmount())
                        .put(CustomerEventTable.TIMESTAMP_COLUMN, customerEvent.getEventTimestamp()));
            }
            // Attach array to root object
            root.set(customerEventRootName, customerEventNode);
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
            System.out.println("Creation of customer event sample data was successful");
        } catch (IOException e){
            System.out.println("Customer events could not be written to json file");
            e.printStackTrace();
        }
    }

    public static List<CustomerEvent> readCustomerEventsFromDirectory(String pathToDir) throws IOException {
        File directory = new File(pathToDir);
        File[] jsonFiles = directory.listFiles();
        List<CustomerEvent> allCustomerEvents = new ArrayList<>();
        if(jsonFiles != null){
            for(File jsonFile : jsonFiles){
                List<CustomerEvent> customerEvents = JsonUtils.readCustomerEvents(jsonFile);
                allCustomerEvents.addAll(customerEvents);
            }
        }
        return allCustomerEvents;
    }

    public static List<Employee> readEmployeesFromDirectory(String pathToDir) throws IOException {
        File directory = new File(pathToDir);
        File[] jsonFiles = directory.listFiles();
        List<Employee> allEmployees = new ArrayList<>();
        if(jsonFiles != null){
            for(File jsonFile : jsonFiles){
                List<Employee> employees = JsonUtils.readEmployees(jsonFile);
                allEmployees.addAll(employees);
            }
        }
        return allEmployees;
    }
}

