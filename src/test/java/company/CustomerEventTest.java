package company;

import org.junit.Test;
import utils.JsonUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerEventTest {

    @Test
    public void testToString() {
        String pathToJson = "json/customer_events_12-04-2026_17-16-03.json";
        List<CustomerEvent> customerEvents = JsonUtils.readCustomerEventsFromJsonFile(pathToJson);
        String expectedString = "Customer event ID: 1, Customer name: Seetrue Technologies, " +
                "Product name: Platform as a Service, " + "Sales amount: 4, Event timestamp: 2025-04-12 17:16:03";
        assertEquals(expectedString, customerEvents.get(0).toString());
    }
}
