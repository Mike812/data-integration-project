package company;

/**
 * Class that provides all necessary methods and variables to create Customer objects for a corresponding table
 *
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String stateName;
    private String stateCode;
    private Long revenue;

    public Customer(int customerId, String customerName, String stateName, String stateCode, Long revenue){
        this.customerId = customerId;
        this.customerName = customerName;
        this.stateName = stateName;
        this.stateCode = stateCode;
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "Customer ID: " + this.customerId + ", Customer name: " + this.customerName + ", " +
                "State name: " + this.stateName + ", State code: " + this.stateCode +
                ", Revenue: " + this.revenue;
    }

    public int getCustomerId(){
        return this.customerId;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public String getStateName(){
        return this.stateName;
    }

    public String getStateCode(){
        return this.stateCode;
    }

    public Long getRevenue(){
        return this.revenue;
    }
}
