package company;

import java.util.List;

import static utils.SqlStatementUtils.*;

/**
 * Interface to a sql table customer_event. Provides create table and insert into table strings.
 */
public interface CustomerEventTable {

    String TABLE_NAME = "customer_event";
    String ID_COLUMN = "id";
    String CUSTOMER_NAME_COLUMN = "customer_name";
    String PRODUCT_NAME_COLUMN = "product_name";
    String SALES_AMOUNT_COLUMN = "sales_amount";
    String TIMESTAMP_COLUMN = "event_timestamp";

    static String getCreateTableString() {
        String createTableString =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +  " (" +
                        ID_COLUMN + " int PRIMARY KEY, " +
                        CUSTOMER_NAME_COLUMN + " VARCHAR(50)," +
                        PRODUCT_NAME_COLUMN + " VARCHAR(50)," +
                        SALES_AMOUNT_COLUMN + " INT," +
                        TIMESTAMP_COLUMN + " VARCHAR(50)" +
                        ");";

        return createTableString;
    }

    static String getInsertIntoTableString(String table, List<CustomerEvent> customerEvents){
        String insertIntoSql = "INSERT INTO " + table + " VALUES ";
        for(CustomerEvent event : customerEvents){
            insertIntoSql += " (" + event.getCustomerEventId() + separatorIntString + event.getCustomerName() +
                    separatorTwoStrings + event.getProductName() + separatorStringInt +
                    event.getSalesAmount() + separatorIntString + event.getEventTimestamp() + backslashString +
                    "),";
        }
        // replace last comma with semicolon
        insertIntoSql = insertIntoSql.substring(0, insertIntoSql.length()-1) + ";";

        return insertIntoSql;
    }

}
