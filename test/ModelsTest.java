public class ModelsTest {
    public static void main(String[] args) {
        System.out.println("Running ModelsTest...");
        
        // Test MenuItem
        MenuItem item = new MenuItem(1, "Chicken Rice Meal", 85.00);
        if (item.getId() != 1 || !item.getName().equals("Chicken Rice Meal") || item.getPrice() != 85.00) {
            throw new AssertionError("MenuItem test failed!");
        }

        // Test Order
        Order order = new Order(1001, "Juan Dela Cruz", "Chicken Rice Meal", 2, 170.00, "PENDING", "2026-10-19 12:00:00");
        if (order.getOrderId() != 1001) throw new AssertionError("Order ID mismatch");
        if (!order.getCustomerName().equals("Juan Dela Cruz")) throw new AssertionError("Customer mismatch");
        if (order.getTotalAmount() != 170.00) throw new AssertionError("Amount mismatch");
        if (!order.getStatus().equals("PENDING")) throw new AssertionError("Status mismatch");

        order.setStatus("SERVED");
        if (!order.getStatus().equals("SERVED")) throw new AssertionError("Status update mismatch");

        System.out.println("ModelsTest PASSED!");
    }
}
