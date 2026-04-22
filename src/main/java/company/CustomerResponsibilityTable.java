package company;

import java.util.List;

import static utils.SqlStatementUtils.*;

/**
 * Interface to a sql table customer_responsibility. Provides create table and insert into table strings.
 */
public interface CustomerResponsibilityTable {

    String TABLE_NAME = "customer_responsibility";
    String EMPLOYEE_ID_COLUMN = "employee_id";
    String EMPLOYEE_NAME_COLUMN = "employee_name";
    String CUSTOMER_ID_COLUMN = "customer_id";
    String CUSTOMER_NAME_COLUMN = "customer_name";
    String PRODUCT_NAME_COLUMN = "product_name";

    static String getCreateTableString() {
        String createTableString =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +  " (" +
                        EMPLOYEE_ID_COLUMN + " int PRIMARY KEY, " +
                        EMPLOYEE_NAME_COLUMN + " VARCHAR(50)," +
                        CUSTOMER_ID_COLUMN + " INT," +
                        CUSTOMER_NAME_COLUMN + " VARCHAR(50)," +
                        PRODUCT_NAME_COLUMN + " VARCHAR(50)" +
                        ");";

        return createTableString;
    }

    static String getInsertIntoTableString(List<CustomerResponsibility> crEntries){
        String insertIntoSql = "INSERT INTO " + TABLE_NAME + " VALUES ";
        for(CustomerResponsibility cr : crEntries){
            insertIntoSql += " (" + cr.getEmployeeId() + separatorIntString + cr.getEmployeeName() +
                    separatorStringInt + cr.getCustomerId() + separatorIntString +
                    cr.getCustomerName() + separatorTwoStrings + cr.getProductName() + backslashString +
                    "),";
        }
        // replace last comma with semicolon
        insertIntoSql = insertIntoSql.substring(0, insertIntoSql.length()-1) + ";";

        return insertIntoSql;
    }

}
