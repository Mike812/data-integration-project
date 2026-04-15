package company;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that provides all necessary methods and variables to create Employee objects for a corresponding table
 * and json files.
 *
 */
public class Employee {

    @JsonProperty(EmployeeTable.ID_COLUMN)
    private int employeeId;
    @JsonProperty(EmployeeTable.NAME_COLUMN)
    private String name;
    @JsonProperty(EmployeeTable.DEPARTMENT_COLUMN)
    private String department;
    @JsonProperty(EmployeeTable.STATE_COLUMN)
    private String state;
    @JsonProperty(EmployeeTable.SALARY_COLUMN)
    private int salary;
    @JsonProperty(EmployeeTable.AGE_COLUMN)
    private int age;
    @JsonProperty(EmployeeTable.BONUS_COLUMN)
    private int bonus;

    // Necessary for json deserialization
    public Employee(){}

    public Employee(int employeeId, String name, String department, String state, int salary, int age, int bonus){
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.state = state;
        this.salary = salary;
        this.age = age;
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Employee ID: " + this.employeeId + ", Name: " + this.name + ", Department: " + this.department +
                ", State: " + this.state + ", Salary: " + this.salary + ", Age: " + this.age + ", Bonus: " + this.bonus;
    }

    public int getEmployeeId(){
        return this.employeeId;
    }

    public String getName(){
        return this.name;
    }

    public String getDepartment(){
        return this.department;
    }

    public String getState(){
        return this.state;
    }

    public int getAge(){
        return this.age;
    }

    public int getSalary(){
        return this.salary;
    }

    public int getBonus(){
        return this.bonus;
    }
}
