package company;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static utils.JsonUtils.readCustomerEventsFromJsonFile;

public class CustomerEventTableTest {

    CustomerEventFactory customerEventFactory = new CustomerEventFactory(null);

    @Test
    public void testGetCreateTableString(){
        String expectedString = "CREATE TABLE IF NOT EXISTS customer_event (id int PRIMARY KEY, " +
                "customer_name VARCHAR(50),product_name VARCHAR(50),sales_amount INT,event_timestamp VARCHAR(50));";

        assertEquals(expectedString, CustomerEventTable.getCreateTableString());
    }

    @Test
    public void testInsertIntoTableString() {
        String pathToJson = "json/customer_events_12-04-2026_17-16-03.json";
        List<CustomerEvent> customerEvents = readCustomerEventsFromJsonFile(pathToJson);
        List<CustomerEvent> customerEventsJsonWithId = customerEventFactory.addIdToCustomerEvents(customerEvents, 0);
        String expectedString = "INSERT INTO customer_event VALUES  (1, 'Seetrue Technologies', 'Platform as a Service', 4, '2025-04-12 17:16:03'), " +
                "(2, 'Cloudsmartz', 'Platform as a Service', 2, '2025-04-12 18:16:03'), " +
                "(3, 'Skelia', 'Software as a Service', 9, '2025-04-12 19:16:03'), " +
                "(4, 'PixelCrayons', 'Software as a Service', 9, '2025-04-12 20:16:03'), " +
                "(5, 'Rye', 'Software as a Service', 2, '2025-04-12 21:16:03'), " +
                "(6, 'Broadcom', 'Software as a Service', 1, '2025-04-12 22:16:03'), " +
                "(7, 'ScienceSoft USA Corporation', 'Software as a Service', 9, '2025-04-12 23:16:03'), " +
                "(8, 'Wisitech InfoSolutions Pvt. Ltd', 'Platform as a Service', 9, '2025-04-13 00:16:03'), " +
                "(9, 'Techasoft', 'Software as a Service', 7, '2025-04-13 01:16:03'), " +
                "(10, 'Earthoptics', 'Platform as a Service', 6, '2025-04-13 02:16:03');";

        String result = CustomerEventTable.getInsertIntoTableString(CustomerEventTable.TABLE_NAME, customerEventsJsonWithId);
        assertEquals(expectedString, result);
    }
}
