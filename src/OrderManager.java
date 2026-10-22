import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderManager acts as the central business logic controller.
 * Coordinates transactions and implements the core algorithms:
 * 1. Queue Enqueue & Dequeue for order processing
 * 2. Linear Search across active and completed records by Order ID
 * 3. Insertion Sort on completed orders by total amount
 */
public class OrderManager {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10;

    private final OrderQueue pendingQueue;
    private final List<Order> servedOrders;
    private final List<MenuItem> menu;
    private int nextOrderId;
    private final DateTimeFormatter timeFormatter;

    public OrderManager() {
        this.pendingQueue = new OrderQueue();
        this.servedOrders = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.nextOrderId = 1001;
        this.timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        initializeMenu();
    }

    /**
     * Pre-populates the canteen menu with typical school canteen meals and drinks.
     */
    private void initializeMenu() {
        menu.add(new MenuItem(1, "Chicken Rice Meal", 85.00));
        menu.add(new MenuItem(2, "Burger Steak w/ Rice", 75.00));
        menu.add(new MenuItem(3, "Pork Tonkatsu Meal", 90.00));
        menu.add(new MenuItem(4, "Spaghetti w/ Garlic Bread", 65.00));
        menu.add(new MenuItem(5, "Crispy French Fries", 40.00));
        menu.add(new MenuItem(6, "Canteen Iced Tea", 25.00));
        menu.add(new MenuItem(7, "Bottled Mineral Water", 15.00));
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public MenuItem getMenuItemById(int id) {
        for (MenuItem item : menu) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * A customer name must contain at least one letter, so blank names and
     * names made only of digits (e.g. "123") are rejected.
     */
    public static boolean isValidCustomerName(String customerName) {
        return customerName != null && customerName.matches(".*\\p{L}.*");
    }

    /**
     * Feature 1: Place Order
     * DSA Applied: Queue — Enqueue
     * Enqueues the newly created order into the pending FIFO queue.
     * Time Complexity: O(1)
     */
    public Order placeOrder(String customerName, int menuItemId, int quantity) {
        MenuItem item = getMenuItemById(menuItemId);
        if (!isValidCustomerName(customerName)
                || item == null || quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            return null;
        }
        customerName = customerName.trim();

        double totalAmount = item.getPrice() * quantity;
        String timestamp = LocalDateTime.now().format(timeFormatter);

        Order newOrder = new Order(nextOrderId++, customerName, item.getName(), quantity, totalAmount, "PENDING", timestamp);
        pendingQueue.enqueue(newOrder);
        return newOrder;
    }

    /**
     * Returns the order at the front of the queue without removing it,
     * or null if the queue is empty.
     */
    public Order peekNextOrder() {
        return pendingQueue.peek();
    }

    /**
     * Feature 2: Serve Next Order
     * DSA Applied: Queue — Dequeue
     * Removes the first pending order from the FIFO queue, marks as SERVED,
     * and adds to the completed history list.
     * Time Complexity: O(1)
     */
    public Order serveNextOrder() {
        Order order = pendingQueue.dequeue();
        if (order != null) {
            order.setStatus("SERVED");
            servedOrders.add(order);
        }
        return order;
    }

    /**
     * Feature 3: Search Order by ID
     * DSA Applied: Linear Search
     * Linearly traverses the pending queue, then the served orders list.
     * Time Complexity: O(n)
     *
     * @param orderId the unique order ID to search for
     * @return the Order if found, or null if non-existent
     */
    public Order linearSearchOrder(int orderId) {
        // Step 1: Linear Search in pending queue
        List<Order> pendingList = pendingQueue.getAllOrders();
        for (Order order : pendingList) {
            if (order.getOrderId() == orderId) {
                return order; // Found in pending queue
            }
        }

        // Step 2: Linear Search in served orders list
        for (Order order : servedOrders) {
            if (order.getOrderId() == orderId) {
                return order; // Found in served records
            }
        }

        return null; // Not found
    }

    /**
     * Feature 4A: View Pending Orders
     * Returns pending orders in strict First-In, First-Out sequence.
     */
    public List<Order> getPendingOrders() {
        return pendingQueue.getAllOrders();
    }

    /**
     * Feature 4B: View Served Orders Sorted by Total Amount
     * DSA Applied: Insertion Sort
     * Creates a copy of served orders and sorts them in-place by totalAmount.
     * Time Complexity: O(n^2) worst/average, O(n) best case
     *
     * @param descending true for highest to lowest, false for lowest to highest
     * @return sorted list of served orders
     */
    public List<Order> getServedOrdersSortedByAmount(boolean descending) {
        List<Order> list = new ArrayList<>(servedOrders);
        int n = list.size();

        for (int i = 1; i < n; i++) {
            Order keyOrder = list.get(i);
            int j = i - 1;

            if (descending) {
                // Shift elements that are smaller than keyOrder
                while (j >= 0 && list.get(j).getTotalAmount() < keyOrder.getTotalAmount()) {
                    list.set(j + 1, list.get(j));
                    j--;
                }
            } else {
                // Shift elements that are larger than keyOrder
                while (j >= 0 && list.get(j).getTotalAmount() > keyOrder.getTotalAmount()) {
                    list.set(j + 1, list.get(j));
                    j--;
                }
            }
            list.set(j + 1, keyOrder);
        }

        return list;
    }

    public List<Order> getServedOrders() {
        return new ArrayList<>(servedOrders);
    }
}
