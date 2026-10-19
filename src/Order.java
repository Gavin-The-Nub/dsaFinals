/**
 * Order encapsulates a customer order in the Canteen Order Queue Management System.
 * Demonstrates OOP principles: Encapsulation (private fields, public accessors).
 */
public class Order {
    private final int orderId;
    private final String customerName;
    private final String itemName;
    private final int quantity;
    private final double totalAmount;
    private String status; // "PENDING" or "SERVED"
    private final String timestamp;

    public Order(int orderId, String customerName, String itemName, int quantity, 
                 double totalAmount, String status, String timestamp) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Order #%d | %s | %s x%d | ₱%.2f | Status: %s | %s",
                orderId, customerName, itemName, quantity, totalAmount, status, timestamp);
    }
}
