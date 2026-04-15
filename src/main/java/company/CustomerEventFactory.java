package company;

import utils.InputOutputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public List<CustomerEvent> createCustomerEventSampleData(int customerEventId, int numberOfEvents){
        List<CustomerEvent> customerEvents = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<numberOfEvents;i++){
            customerEventId += 1;
            String customerName = this.customerNames.get(random.nextInt(customerNames.size()));
            String productName = this.productNames[random.nextInt(this.productNames.length)];
            int salesAmount = this.salesAmounts[random.nextInt(this.salesAmounts.length)];
            String eventTimestamp = InputOutputUtils.getTimestampForSampleData("yyyy-MM-dd HH:mm:ss", i, numberOfEvents);

            customerEvents.add(new CustomerEvent(customerEventId, customerName, productName, salesAmount,
                    eventTimestamp));
        }

        return customerEvents;
    }
}
