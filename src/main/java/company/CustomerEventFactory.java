package company;

import utils.InputOutputUtils;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Factory class that creates CustomerEvent objects with random sample data.
 */
public class CustomerEventFactory {

    List<String> customerNames;
    String[] productNames;
    int[] salesAmounts;

    public CustomerEventFactory(){
        this.customerNames = InputOutputUtils.getCompanyNamesFromFile();
        this.productNames = new String[]{"Software as a Service", "Platform as a Service"};
        this.salesAmounts = IntStream.range(1, 10).toArray();
    }

    public List<CustomerEvent> createCustomerEventSampleData(int customerEventId, int numberOfEvents, boolean databaseEntry){
        List<CustomerEvent> customerEvents = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<numberOfEvents;i++){
            customerEventId += 1;
            String customerName = this.customerNames.get(random.nextInt(customerNames.size()));
            String productName = this.productNames[random.nextInt(this.productNames.length)];
            int salesAmount = this.salesAmounts[random.nextInt(this.salesAmounts.length)];
            String eventTimestamp = InputOutputUtils.getTimestampForSampleData("yyyy-MM-dd HH:mm:ss", i, numberOfEvents);

            if(databaseEntry){
                customerEvents.add(new CustomerEvent(customerEventId, customerName, productName, salesAmount,
                        eventTimestamp));
            } else {
                customerEvents.add(new CustomerEvent(customerName, productName, salesAmount, eventTimestamp));
            }
        }

        return customerEvents;
    }

    public static List<CustomerEvent> addIdToCustomerEvents(List<CustomerEvent> customerEvents, int maxId){
        List<CustomerEvent> customerEventsWithId = new ArrayList<>();
        for (CustomerEvent customerEvent : customerEvents){
            maxId += 1;
            customerEvent.setCustomerEventId(maxId);
            customerEventsWithId.add(customerEvent);
        }

        return customerEventsWithId;
    }

    public static Map<String, Integer> getMapWithSummedUpSalesAmounts(List<CustomerEvent> customerEvents){
        Map<String, Integer> customerSalesMap = new HashMap<>();
        for(CustomerEvent customerEvent : customerEvents){
            String key = customerEvent.getCustomerName() + "_" + customerEvent.getProductName();
            if(customerSalesMap.get(key) != null){
                customerSalesMap.put(key, customerSalesMap.get(key) + customerEvent.getSalesAmount());
            } else {
                customerSalesMap.put(key, customerEvent.getSalesAmount());
            }
        }

        return customerSalesMap;
    }

    public static List<CustomerEvent> getListWithSummedUpSalesAmounts(List<CustomerEvent> customerEvents, String currentTimestamp){
        Map<String, Integer> summedUpSalesAmounts = getMapWithSummedUpSalesAmounts(customerEvents);

        List<CustomerEvent> customerEventsAggregated = new ArrayList<>();
        for(Map.Entry<String, Integer> mapEntry : summedUpSalesAmounts.entrySet()){
            String[] customerAndProductNames = mapEntry.getKey().split("_");
            String customerName = customerAndProductNames[0];
            String productName = customerAndProductNames[1];
            int salesAmount = mapEntry.getValue();
            customerEventsAggregated.add(new CustomerEvent(customerName, productName, salesAmount, currentTimestamp));
        }

        return customerEventsAggregated;
    }
}
