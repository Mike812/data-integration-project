package company;

import org.slf4j.LoggerFactory;
import utils.InputOutputUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Factory class that creates CustomerEvent objects with random sample data.
 */
public class CustomerEventFactory {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CustomerEventFactory.class);
    List<String> customerNames;
    String[] productNames;
    int[] salesAmounts;
    Logger logger;

    public CustomerEventFactory(Logger logger){
        this.customerNames = InputOutputUtils.getCompanyNamesFromFile();
        this.productNames = new String[]{"Software as a Service", "Platform as a Service"};
        this.salesAmounts = IntStream.range(1, 10).toArray();
        if(logger == null){
            this.logger = Logger.getLogger("CustomerEventFactory Log");
        } else{
            this.logger = logger;
        }
    }

    public List<CustomerEvent> createCustomerEventSampleData(int customerEventId, int numberOfEvents, boolean databaseEntry){
        logger.info("Create customer event sample data");
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

    public List<CustomerEvent> addIdToCustomerEvents(List<CustomerEvent> customerEvents, int maxId){
        logger.info("Add id to customer events");
        List<CustomerEvent> customerEventsWithId = new ArrayList<>();
        for (CustomerEvent customerEvent : customerEvents){
            maxId += 1;
            customerEvent.setCustomerEventId(maxId);
            customerEventsWithId.add(customerEvent);
        }

        return customerEventsWithId;
    }

    // Group kafka consumer records by customer and product name and sum up the sales amounts
    public Map<String, Integer> getMapWithSummedUpSalesAmounts(List<CustomerEvent> customerEvents){
        logger.info("Create map with summed up sales amounts");
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

    // Reproduce all customer fields after aggregating sales amounts
    public List<CustomerEvent> getListWithSummedUpSalesAmounts(List<CustomerEvent> customerEvents, String currentTimestamp){
        logger.info("Reproduce customer events after aggregating sales amounts");
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


    public List<CustomerEvent> createCustomerEventListFromSqlResult(ResultSet result) {
        logger.info("Create customer event list from sql result set");
        List<CustomerEvent> customerEvents = new ArrayList<>();
        try {
            while(result.next()){
                customerEvents.add(
                        new CustomerEvent(result.getInt(CustomerEventTable.ID_COLUMN),
                                result.getString(CustomerEventTable.CUSTOMER_NAME_COLUMN),
                                result.getString(CustomerEventTable.PRODUCT_NAME_COLUMN),
                                result.getInt(CustomerEventTable.SALES_AMOUNT_COLUMN),
                                result.getString(CustomerEventTable.TIMESTAMP_COLUMN)));
            }
        } catch (SQLException e) {
            logger.info("Create list from sql result set failed");
            e.printStackTrace();
        }

        return customerEvents;
    }
}
