package company;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that provides all necessary methods and variables to create CustomerEvent objects for a corresponding table
 * and json files.
 *
 */
public class CustomerEvent {

    private int customerEventId;
    @JsonProperty(CustomerEventTable.CUSTOMER_NAME_COLUMN)
    private String customerName;
    @JsonProperty(CustomerEventTable.PRODUCT_NAME_COLUMN)
    private String productName;
    @JsonProperty(CustomerEventTable.SALES_AMOUNT_COLUMN)
    private int salesAmount;
    @JsonProperty(CustomerEventTable.TIMESTAMP_COLUMN)
    private String eventTimestamp;

    // Necessary for json deserialization
    public CustomerEvent() {}

    public CustomerEvent(String customerName, String productName, int salesAmount, String eventTimestamp){
        this.customerName = customerName;
        this.productName = productName;
        this.salesAmount = salesAmount;
        this.eventTimestamp = eventTimestamp;
    }

    public CustomerEvent(int customerEventId, String customerName, String productName, int salesAmount,
                         String eventTimestamp){
        this.customerEventId = customerEventId;
        this.customerName = customerName;
        this.productName = productName;
        this.salesAmount = salesAmount;
        this.eventTimestamp = eventTimestamp;
    }

    @Override
    public String toString() {
        return "Customer event ID: " + this.customerEventId + ", Customer name: " + this.customerName + ", " +
                "Product name: " + this.productName + ", Sales amount: " + this.salesAmount +
                ", Event timestamp: " + this.eventTimestamp;
    }

    public int getCustomerEventId(){
        return this.customerEventId;
    }

    public void setCustomerEventId(int customerEventId){
        this.customerEventId = customerEventId;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public String getProductName(){
        return this.productName;
    }

    public int getSalesAmount(){
        return this.salesAmount;
    }

    public String getEventTimestamp(){
        return this.eventTimestamp;
    }
}
