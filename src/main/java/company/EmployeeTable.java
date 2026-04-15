package company;

import java.util.List;

import static utils.SqlStatementUtils.*;

/**
 * Interface to a sql table employee. Provides create table and insert into table strings.
 */
public interface EmployeeTable {

    String TABLE_NAME = "employee";
    String ID_COLUMN = "employee_id";
    String NAME_COLUMN = "name";
    String DEPARTMENT_COLUMN = "department";
    String STATE_COLUMN = "state";
    String SALARY_COLUMN = "salary";
    String AGE_COLUMN = "age";
    String BONUS_COLUMN = "bonus";

    static String getCreateTableString() {
        String createTableString =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +  " (" +
                        ID_COLUMN + " int PRIMARY KEY, " +
                        NAME_COLUMN + " VARCHAR(50)," +
                        DEPARTMENT_COLUMN + " VARCHAR(50)," +
                        STATE_COLUMN + " VARCHAR(2)," +
                        SALARY_COLUMN + " INT," +
                        AGE_COLUMN + " INT," +
                        BONUS_COLUMN + " INT" +
                        ");";

        return createTableString;
    }

    static String getInsertIntoTableString(String table, List<Employee> employees){
        String insertIntoSql = "INSERT INTO " + table + " VALUES ";
        for(Employee employee : employees){
            insertIntoSql += " (" + employee.getEmployeeId() + fieldSeparatorString + employee.getName() +
                    fieldSeparatorTwoStrings + employee.getDepartment() + fieldSeparatorTwoStrings +
                    employee.getState() + fieldSeparatorStringInt + employee.getSalary() + fieldSeparatorInt +
                    employee.getAge() + fieldSeparatorInt + employee.getBonus() + "),";
        }
        // replace last comma with semicolon
        insertIntoSql = insertIntoSql.substring(0, insertIntoSql.length()-1) + ";";

        return insertIntoSql;
    }
}
