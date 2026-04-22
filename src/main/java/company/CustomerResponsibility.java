package company;

/**
 * Class that provides all necessary methods and variables to create CustomerResponsibility objects for a corresponding table
 *
 */
public class CustomerResponsibility {

    private int employeeId;
    private String employeeName;
    private int customerId;
    private String customerName;
    private String productName;

    public CustomerResponsibility(int employeeId, String employeeName, int customerID, String customerName, String productName){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.customerId = customerID;
        this.customerName = customerName;
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Employee ID: " + this.employeeId + ", Employee Name: " + this.employeeName + ", " + "Customer ID: " + this.customerId +
                ", Customer name: " + this.customerName + ", " + "Product name: " + this.productName;
    }

    public int getEmployeeId(){
        return this.employeeId;
    }

    public String getEmployeeName(){
        return this.employeeName;
    }

    public int getCustomerId(){
        return this.customerId;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public String getProductName(){
        return this.productName;
    }
}
