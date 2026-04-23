package company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Factory class that creates CustomerResponsibility objects with random sample data.
 */
public class CustomerResponsibilityFactory {

    List<Employee> employees;
    List<Customer> customers;
    String[] productNames;
    Logger logger;

    public CustomerResponsibilityFactory(List<Employee> employees, List<Customer> customers, Logger logger){
        this.employees = employees;
        this.customers = customers;
        this.productNames = new String[]{"Software as a Service", "Platform as a Service"};
        if(logger == null){
            this.logger = Logger.getLogger("CustomerResponsibilityFactory Log");
        } else{
            this.logger = logger;
        }
    }

    public List<CustomerResponsibility> createCustomerResponsibilitySampleData(){
        logger.info("Create customer responsibility sample data");
        List<CustomerResponsibility> customerResponsibilities = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<this.employees.size();i++){
            Employee employee = employees.get(i);
            int employeeId = employee.getEmployeeId();
            String employeeName = employee.getName();
            int randomCustomer = random.nextInt(this.customers.size());
            int customerId = getCustomerIds(this.customers).get(randomCustomer);
            String customerName = getCustomerNames(this.customers).get(randomCustomer);
            String productName = this.productNames[random.nextInt(this.productNames.length)];
            customerResponsibilities.add(new CustomerResponsibility(employeeId, employeeName, customerId, customerName, productName));
        }

        return customerResponsibilities;
    }

    public List<Integer> getCustomerIds(List<Customer> customers){
        List<Integer> customerIds = new ArrayList<>();
        for(Customer customer : customers){
            customerIds.add(customer.getCustomerId());
        }

        return customerIds;
    }

    public List<String> getCustomerNames(List<Customer> customers){
        List<String> customerNames = new ArrayList<>();
        for(Customer customer : customers){
            customerNames.add(customer.getCustomerName());
        }

        return customerNames;
    }
}
