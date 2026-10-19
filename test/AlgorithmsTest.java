import java.util.List;

public class AlgorithmsTest {
    public static void main(String[] args) {
        System.out.println("Running AlgorithmsTest...");
        OrderManager manager = new OrderManager();

        // 1. Verify default Menu
        List<MenuItem> menu = manager.getMenu();
        if (menu.isEmpty()) throw new AssertionError("Menu should not be empty");

        // 2. Place Orders (Queue Enqueue)
        Order o1 = manager.placeOrder("Juan", 1, 1); // ₱85.00
        Order o2 = manager.placeOrder("Maria", 2, 2); // ₱75.00 * 2 = ₱150.00
        Order o3 = manager.placeOrder("Pedro", 6, 1); // ₱25.00

        if (manager.getPendingOrders().size() != 3) throw new AssertionError("Pending orders count should be 3");

        // 3. Test Linear Search in pending queue
        Order found1 = manager.linearSearchOrder(o2.getOrderId());
        if (found1 == null || !found1.getCustomerName().equals("Maria")) {
            throw new AssertionError("Linear search failed to find pending order");
        }

        // 4. Serve Orders (Queue Dequeue)
        Order served1 = manager.serveNextOrder();
        if (served1.getOrderId() != o1.getOrderId() || !served1.getStatus().equals("SERVED")) {
            throw new AssertionError("First served order should be o1 and status SERVED");
        }

        // 5. Test Linear Search in served list
        Order foundServed = manager.linearSearchOrder(o1.getOrderId());
        if (foundServed == null || !foundServed.getStatus().equals("SERVED")) {
            throw new AssertionError("Linear search failed to find served order");
        }

        // Search non-existent ID
        if (manager.linearSearchOrder(9999) != null) {
            throw new AssertionError("Linear search should return null for non-existent order");
        }

        // Serve remaining orders
        manager.serveNextOrder(); // Maria: ₱150.00
        manager.serveNextOrder(); // Pedro: ₱25.00

        // 6. Test Insertion Sort on served orders (Descending: ₱150.00, ₱85.00, ₱25.00)
        List<Order> sortedDesc = manager.getServedOrdersSortedByAmount(true);
        if (sortedDesc.size() != 3) throw new AssertionError("Sorted list size mismatch");
        if (sortedDesc.get(0).getTotalAmount() != 150.00) throw new AssertionError("Highest amount should be 150");
        if (sortedDesc.get(1).getTotalAmount() != 85.00) throw new AssertionError("Middle amount should be 85");
        if (sortedDesc.get(2).getTotalAmount() != 25.00) throw new AssertionError("Lowest amount should be 25");

        // Test Insertion Sort Ascending (₱25.00, ₱85.00, ₱150.00)
        List<Order> sortedAsc = manager.getServedOrdersSortedByAmount(false);
        if (sortedAsc.get(0).getTotalAmount() != 25.00 || sortedAsc.get(2).getTotalAmount() != 150.00) {
            throw new AssertionError("Ascending insertion sort failed");
        }

        System.out.println("AlgorithmsTest PASSED!");
    }
}
