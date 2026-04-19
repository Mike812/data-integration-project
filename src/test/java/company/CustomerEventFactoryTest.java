package company;

import org.junit.Test;
import utils.JsonUtils;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CustomerEventFactoryTest {

    String pathToJson = "json/customer_events_19-04-2026_13-16-03.json";
    List<CustomerEvent> customerEvents = JsonUtils.readCustomerEventsFromJsonFile(pathToJson);

    @Test
    public void testGetMapWithSummedUpSalesAmounts(){
        Map<String, Integer> salesAmountMap = CustomerEventFactory.getMapWithSummedUpSalesAmounts(customerEvents);
        int salesAmountSeetrue = salesAmountMap.get("Seetrue Technologies_Platform as a Service");
        assertEquals(8, salesAmountSeetrue);
        // assertThrows(NullPointerException.class, () -> salesAmountMap.get("Seetrue Technologies_Software as a Service"));

        int salesAmountTech = salesAmountMap.get("Techasoft_Software as a Service");
        assertEquals(20, salesAmountTech);
        int salesAmountTech2 = salesAmountMap.get("Techasoft_Platform as a Service");
        assertEquals(6, salesAmountTech2);

        int salesAmountEarth = salesAmountMap.get("Earthoptics_Software as a Service");
        assertEquals(6, salesAmountEarth);
        int salesAmountEarth2 = salesAmountMap.get("Earthoptics_Platform as a Service");
        assertEquals(6, salesAmountEarth2);
    }

    @Test
    public void testGetListWithSummedUpSalesAmounts(){
        List<CustomerEvent> customerEventsAgg = CustomerEventFactory.getListWithSummedUpSalesAmounts(customerEvents,"19-04-2026_13-16-03");
        assertEquals(6, customerEventsAgg.size());
        int summedUpSalesAmount = 0;
        for(CustomerEvent customerEvent : customerEventsAgg){
            summedUpSalesAmount += customerEvent.getSalesAmount();
        }
        assertEquals(55, summedUpSalesAmount);

        CustomerEvent techasoft = customerEventsAgg.get(0);
        assertEquals("Techasoft", techasoft.getCustomerName());
        assertEquals("Software as a Service", techasoft.getProductName());
        assertEquals(20, techasoft.getSalesAmount());

        CustomerEvent techasoft2 = customerEventsAgg.get(1);
        assertEquals("Techasoft", techasoft2.getCustomerName());
        assertEquals("Platform as a Service", techasoft2.getProductName());
        assertEquals(6, techasoft2.getSalesAmount());
    }
}
