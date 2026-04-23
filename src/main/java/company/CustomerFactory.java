package company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.LongStream;

/**
 * Factory class that creates Customer objects with random sample data.
 */
public class CustomerFactory {

    List<String> customerNames;
    List<String> stateNames;
    List<String> statesCodes;
    long[] revenues;
    Logger logger;

    public CustomerFactory(Logger logger){
        DataFactory dataFactory = DataFactory.getInstance();
        this.customerNames = dataFactory.getCompanyNames();
        this.stateNames = dataFactory.getStateNames();
        this.statesCodes = dataFactory.getStateCodes();
        long oneBillion = 1000000000L;
        this.revenues = new long[]{oneBillion, oneBillion * 2, oneBillion * 3, oneBillion * 4, oneBillion * 5, oneBillion * 6};
        if(logger == null){
            this.logger = Logger.getLogger("CustomerFactory Log");
        } else{
            this.logger = logger;
        }
    }

    public List<Customer> createCustomerSampleData(){
        logger.info("Create customer sample data");
        List<Customer> customers = new ArrayList<>();
        Random random = new Random();
        int customerId = 0;
        for(int i=0;i<this.customerNames.size();i++){
            customerId += 1;
            String customerName = this.customerNames.get(i);
            int randomStateInfo = random.nextInt(this.stateNames.size());
            String stateName = this.stateNames.get(randomStateInfo);
            String stateCode = this.statesCodes.get(randomStateInfo);
            Long revenue = this.revenues[random.nextInt(this.revenues.length)];
            customers.add(new Customer(customerId, customerName, stateName, stateCode, revenue));
        }

        return customers;
    }
}
