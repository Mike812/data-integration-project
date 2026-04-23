package company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
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
    Logger logger;

    public EmployeeFactory(Logger logger){
        DataFactory dataFactory = DataFactory.getInstance();
        this.firstNames = dataFactory.getFirstNames();
        this.lastNames = dataFactory.getLastNames();
        this.departments = new String[]{"Finance", "Marketing", "Sales", "Accounting", "IT"};
        this.states = dataFactory.getStateCodes();
        this.salary = IntStream.range(50000, 100000).toArray();
        this.age = IntStream.range(20, 60).toArray();
        this.bonus = new int[]{1000, 2000, 5000, 10000, 15000};
        if(logger == null){
            this.logger = Logger.getLogger("EmployeeFactory Log");
        } else{
            this.logger = logger;
        }

    }

    public List<Employee> createEmployeeSampleData(int employeeId, int numberOfEmployees, boolean databaseEntry){
        logger.info("Create employee sample data");
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
                employees.add(new Employee(employeeId, name, department, state, salary, age, bonus));
            } else {
                employees.add(new Employee(name, department, state, salary, age, bonus));
            }
        }

        return employees;
    }

    public List<Employee> addIdToEmployees(List<Employee> employees, int maxId){
        logger.info("Add id to employees");
        List<Employee> employeesWithId = new ArrayList<>();
        for (Employee employee : employees){
            maxId += 1;
            employee.setEmployeeId(maxId);
            employeesWithId.add(employee);
        }

        return employeesWithId;
    }


    public List<Employee> createEmployeeListFromSqlResult(ResultSet result) {
        logger.info("Create employee list from sql result set");
        List<Employee> employees = new ArrayList<>();
        try {
            while(result.next()){
                employees.add(
                        new Employee(result.getInt(EmployeeTable.ID_COLUMN),
                                result.getString(EmployeeTable.NAME_COLUMN),
                                result.getString(EmployeeTable.DEPARTMENT_COLUMN),
                                result.getString(EmployeeTable.STATE_COLUMN),
                                result.getInt(EmployeeTable.SALARY_COLUMN),
                                result.getInt(EmployeeTable.AGE_COLUMN),
                                result.getInt(EmployeeTable.BONUS_COLUMN)));
            }
        } catch (SQLException e) {
            logger.info("Create list from sql result set failed");
            e.printStackTrace();
        }

        return employees;
    }
}
