package company;

import java.util.List;

import static utils.SqlStatementUtils.*;

/**
 * Interface to a sql table customer. Provides create table and insert into table strings.
 */
public interface CustomerTable {

    String TABLE_NAME = "customer";
    String CUSTOMER_ID_COLUMN = "customer_id";
    String CUSTOMER_NAME_COLUMN = "customer_name";
    String STATE_NAME_COLUMN = "state_name";
    String STATE_CODE_COLUMN = "state_code";
    String REVENUE_COLUMN = "revenue";

    static String getCreateTableString() {
        String createTableString =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +  " (" +
                        CUSTOMER_ID_COLUMN + " int PRIMARY KEY, " +
                        CUSTOMER_NAME_COLUMN + " VARCHAR(50)," +
                        STATE_NAME_COLUMN + " VARCHAR(50)," +
                        STATE_CODE_COLUMN + " VARCHAR(50)," +
                        REVENUE_COLUMN + " BIGINT" +
                        ");";

        return createTableString;
    }

    static String getInsertIntoTableString(List<Customer> customers){
        String insertIntoSql = "INSERT INTO " + TABLE_NAME + " VALUES ";
        for(Customer customer : customers){
            insertIntoSql += " (" + customer.getCustomerId() + separatorIntString + customer.getCustomerName() +
                    separatorTwoStrings + customer.getStateName() + separatorTwoStrings +
                    customer.getStateCode() + separatorStringInt + customer.getRevenue() +
                    "),";
        }
        // replace last comma with semicolon
        insertIntoSql = insertIntoSql.substring(0, insertIntoSql.length()-1) + ";";

        return insertIntoSql;
    }

}
