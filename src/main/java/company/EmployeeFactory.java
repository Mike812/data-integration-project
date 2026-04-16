package company;

import utils.InputOutputUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Factory class that creates Employee objects with random sample data.
 */
public class EmployeeFactory {

    List<String> firstNames;
    List<String> lastNames;
    String[] departments;
    List<String> states;
    int[] salary;
    int[] age;
    int[] bonus;

    public EmployeeFactory(){
        this.firstNames = InputOutputUtils.getFirstNamesFromFile();
        this.lastNames = InputOutputUtils.getLastNamesFromFile();
        this.departments = new String[]{"Finance", "Marketing", "Sales", "Accounting", "IT"};
        this.states = InputOutputUtils.getUsStateTwoLetterCodesFromFile();
        this.salary = IntStream.range(50000, 100000).toArray();
        this.age = IntStream.range(20, 60).toArray();
        this.bonus = new int[]{1000, 2000, 5000, 10000, 15000};
    }

    public List<Employee> createEmployeeSampleData(int employeeId, int numberOfEmployees, boolean databaseEntry){
        List<Employee> employees = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<numberOfEmployees;i++){
            employeeId += 1;
            String name = this.firstNames.get(random.nextInt(firstNames.size())) + " " +
                    this.lastNames.get(random.nextInt(lastNames.size()));
            String department = this.departments[random.nextInt(this.departments.length)];
            String state = this.states.get(random.nextInt(this.states.size()));
            int salary = this.salary[random.nextInt(this.salary.length)];
            int age = this.age[random.nextInt(this.age.length)];
            int bonus = this.bonus[random.nextInt(this.bonus.length)];

            if(databaseEntry){
                employees.add(new Employee(name, department, state, salary, age, bonus));
            } else {
                employees.add(new Employee(employeeId, name, department, state, salary, age, bonus));
            }
        }

        return employees;
    }

    public static List<Employee> addIdToEmployees(List<Employee> employees, int maxId){
        List<Employee> employeesWithId = new ArrayList<>();
        for (Employee employee : employees){
            maxId += 1;
            employeesWithId.add(
                    new Employee(maxId, employee.getName(), employee.getDepartment(), employee.getState(),
                            employee.getSalary(), employee.getAge(), employee.getBonus()));
        }

        return employeesWithId;
    }
}
