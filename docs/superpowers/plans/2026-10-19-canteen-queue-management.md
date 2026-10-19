# Canteen Order Queue Management System Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a modular, pure Java Canteen Order Queue Management System demonstrating a custom FIFO Queue, Linear Search, and Insertion Sort for the De La Salle Lipa Datastrc Final Project.

**Architecture:** Model-Driven Object-Oriented Architecture separating models (`MenuItem`, `Order`), custom pointer-based data structures (`OrderNode`, `OrderQueue`), business logic with algorithms (`OrderManager`), and an interactive terminal CLI (`Main`). A zero-dependency test suite verifies all data structures and algorithms.

**Tech Stack:** Pure Java (JDK 25 LTS compatible, zero external dependencies).

## Global Constraints

- Pure standard Java only (no Node.js, no external JARs, standard library only).
- Queue must be built from scratch using linked nodes (`OrderNode`) with `enqueue` and `dequeue`.
- Linear Search must search across both pending queue and served orders by Order ID.
- Insertion Sort must sort completed orders by total price.
- All code formatted cleanly with proper Java conventions and comments for documentation.

---

### Task 1: Core Domain Models (`MenuItem` and `Order`)

**Files:**
- Create: `src/MenuItem.java`
- Create: `src/Order.java`
- Test: `test/ModelsTest.java`

**Interfaces:**
- Produces:
  - `MenuItem(int id, String name, double price)`
  - `MenuItem.getId()`, `getName()`, `getPrice()`
  - `Order(int orderId, String customerName, String itemName, int quantity, double totalAmount, String status, String timestamp)`
  - `Order.getOrderId()`, `getCustomerName()`, `getItemName()`, `getQuantity()`, `getTotalAmount()`, `getStatus()`, `setStatus(String)`

- [ ] **Step 1: Write the failing test for models**

```java
// test/ModelsTest.java
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `javac -d out test/ModelsTest.java`
Expected: Compilation failure because `MenuItem` and `Order` do not exist yet.

- [ ] **Step 3: Implement `MenuItem.java` and `Order.java`**

```java
// src/MenuItem.java
public class MenuItem {
    private final int id;
    private final String name;
    private final double price;

    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("[%d] %-22s - ₱%.2f", id, name, price);
    }
}
```

```java
// src/Order.java
public class Order {
    private final int orderId;
    private final String customerName;
    private final String itemName;
    private final int quantity;
    private final double totalAmount;
    private String status;
    private final String timestamp;

    public Order(int orderId, String customerName, String itemName, int quantity, double totalAmount, String status, String timestamp) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Order #%d | %s | %s x%d | ₱%.2f | Status: %s | %s",
                orderId, customerName, itemName, quantity, totalAmount, status, timestamp);
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `javac -d out src/MenuItem.java src/Order.java test/ModelsTest.java && java -cp out ModelsTest`
Expected: Output `ModelsTest PASSED!`

- [ ] **Step 5: Commit**

```bash
git add src/MenuItem.java src/Order.java test/ModelsTest.java
git commit -m "feat: add MenuItem and Order domain models with unit test"
```

---

### Task 2: Pure Java Custom Linked Queue (`OrderNode` & `OrderQueue`)

**Files:**
- Create: `src/OrderNode.java`
- Create: `src/OrderQueue.java`
- Test: `test/QueueTest.java`

**Interfaces:**
- Consumes: `Order` from Task 1
- Produces:
  - `OrderNode(Order data)`
  - `OrderQueue()`
  - `void enqueue(Order order)`
  - `Order dequeue()`
  - `Order peek()`
  - `boolean isEmpty()`
  - `int getSize()`
  - `List<Order> getAllOrders()`

- [ ] **Step 1: Write the failing test for `OrderQueue`**

```java
// test/QueueTest.java
import java.util.List;

public class QueueTest {
    public static void main(String[] args) {
        System.out.println("Running QueueTest...");
        OrderQueue queue = new OrderQueue();

        if (!queue.isEmpty()) throw new AssertionError("Queue should be initially empty");
        if (queue.getSize() != 0) throw new AssertionError("Initial size should be 0");
        if (queue.dequeue() != null) throw new AssertionError("Dequeue from empty queue should return null");

        Order o1 = new Order(1001, "Alice", "Burger", 1, 50.00, "PENDING", "12:00");
        Order o2 = new Order(1002, "Bob", "Fries", 2, 70.00, "PENDING", "12:01");
        Order o3 = new Order(1003, "Charlie", "Drink", 1, 25.00, "PENDING", "12:02");

        queue.enqueue(o1);
        queue.enqueue(o2);
        queue.enqueue(o3);

        if (queue.getSize() != 3) throw new AssertionError("Size should be 3");
        if (queue.peek() != o1) throw new AssertionError("Peek should return first order");

        // Verify FIFO order with getAllOrders
        List<Order> list = queue.getAllOrders();
        if (list.size() != 3 || list.get(0) != o1 || list.get(1) != o2 || list.get(2) != o3) {
            throw new AssertionError("getAllOrders did not preserve FIFO order");
        }

        // Dequeue verification
        Order dequeued1 = queue.dequeue();
        if (dequeued1 != o1) throw new AssertionError("First dequeue should be o1");
        if (queue.getSize() != 2) throw new AssertionError("Size should be 2 after 1 dequeue");

        Order dequeued2 = queue.dequeue();
        if (dequeued2 != o2) throw new AssertionError("Second dequeue should be o2");

        Order dequeued3 = queue.dequeue();
        if (dequeued3 != o3) throw new AssertionError("Third dequeue should be o3");

        if (!queue.isEmpty()) throw new AssertionError("Queue should be empty now");
        if (queue.dequeue() != null) throw new AssertionError("Dequeue from empty should be null");

        System.out.println("QueueTest PASSED!");
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `javac -d out -cp out test/QueueTest.java`
Expected: Compilation failure because `OrderQueue` and `OrderNode` are missing.

- [ ] **Step 3: Implement `OrderNode.java` and `OrderQueue.java`**

```java
// src/OrderNode.java
public class OrderNode {
    public Order data;
    public OrderNode next;

    public OrderNode(Order data) {
        this.data = data;
        this.next = null;
    }
}
```

```java
// src/OrderQueue.java
import java.util.ArrayList;
import java.util.List;

public class OrderQueue {
    private OrderNode front;
    private OrderNode rear;
    private int size;

    public OrderQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(Order order) {
        OrderNode newNode = new OrderNode(order);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public Order dequeue() {
        if (isEmpty()) {
            return null;
        }
        Order removedOrder = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return removedOrder;
    }

    public Order peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        OrderNode current = front;
        while (current != null) {
            orders.add(current.data);
            current = current.next;
        }
        return orders;
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `javac -d out src/OrderNode.java src/OrderQueue.java test/QueueTest.java && java -cp out QueueTest`
Expected: Output `QueueTest PASSED!`

- [ ] **Step 5: Commit**

```bash
git add src/OrderNode.java src/OrderQueue.java test/QueueTest.java
git commit -m "feat: implement pure Java custom FIFO OrderQueue with unit test"
```

---

### Task 3: Business Logic & Algorithms (`OrderManager`)

**Files:**
- Create: `src/OrderManager.java`
- Test: `test/AlgorithmsTest.java`

**Interfaces:**
- Consumes: `Order`, `MenuItem`, `OrderQueue` from Tasks 1 & 2
- Produces:
  - `OrderManager()`
  - `Order placeOrder(String customerName, int menuItemId, int quantity)`
  - `Order serveNextOrder()`
  - `Order linearSearchOrder(int orderId)`
  - `List<Order> getPendingOrders()`
  - `List<Order> getServedOrdersSortedByAmount(boolean descending)`
  - `List<MenuItem> getMenu()`
  - `MenuItem getMenuItemById(int id)`

- [ ] **Step 1: Write the failing test for `OrderManager` algorithms**

```java
// test/AlgorithmsTest.java
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
        Order o3 = manager.placeOrder("Pedro", 4, 1); // ₱25.00

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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `javac -d out -cp out test/AlgorithmsTest.java`
Expected: Compilation failure because `OrderManager` is missing.

- [ ] **Step 3: Implement `OrderManager.java`**

```java
// src/OrderManager.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
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

    // Feature 1: Place Order (Queue Enqueue)
    public Order placeOrder(String customerName, int menuItemId, int quantity) {
        MenuItem item = getMenuItemById(menuItemId);
        if (item == null || quantity <= 0) {
            return null;
        }

        double totalAmount = item.getPrice() * quantity;
        String timestamp = LocalDateTime.now().format(timeFormatter);

        Order newOrder = new Order(nextOrderId++, customerName, item.getName(), quantity, totalAmount, "PENDING", timestamp);
        pendingQueue.enqueue(newOrder);
        return newOrder;
    }

    // Feature 2: Serve Next Order (Queue Dequeue)
    public Order serveNextOrder() {
        Order order = pendingQueue.dequeue();
        if (order != null) {
            order.setStatus("SERVED");
            servedOrders.add(order);
        }
        return order;
    }

    // Feature 3: Search Order by ID (Linear Search)
    public Order linearSearchOrder(int orderId) {
        // Step 1: Linear Search through pending queue
        List<Order> pendingList = pendingQueue.getAllOrders();
        for (Order order : pendingList) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        // Step 2: Linear Search through served orders
        for (Order order : servedOrders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null; // Not found
    }

    // Feature 4A: View Pending Orders (FIFO Queue Traversal)
    public List<Order> getPendingOrders() {
        return pendingQueue.getAllOrders();
    }

    // Feature 4B: View Served Orders Sorted by Amount (Insertion Sort)
    public List<Order> getServedOrdersSortedByAmount(boolean descending) {
        List<Order> list = new ArrayList<>(servedOrders);
        int n = list.size();

        // Standard Insertion Sort algorithm
        for (int i = 1; i < n; i++) {
            Order keyOrder = list.get(i);
            int j = i - 1;

            if (descending) {
                while (j >= 0 && list.get(j).getTotalAmount() < keyOrder.getTotalAmount()) {
                    list.set(j + 1, list.get(j));
                    j--;
                }
            } else {
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `javac -d out src/OrderManager.java test/AlgorithmsTest.java && java -cp out AlgorithmsTest`
Expected: Output `AlgorithmsTest PASSED!`

- [ ] **Step 5: Commit**

```bash
git add src/OrderManager.java test/AlgorithmsTest.java
git commit -m "feat: implement OrderManager with Linear Search and Insertion Sort"
```

---

### Task 4: Interactive CLI (`Main.java`) & Verification

**Files:**
- Create: `src/Main.java`

**Interfaces:**
- Consumes: `OrderManager`, `Order`, `MenuItem`
- Entry Point: `public static void main(String[] args)`

- [ ] **Step 1: Implement `Main.java` with input validation, formatted tables, and menu loop**

```java
// src/Main.java
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderManager manager = new OrderManager();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printHeader();
            System.out.println("[1] Place Order (Enqueue)");
            System.out.println("[2] Serve Next Order (Dequeue)");
            System.out.println("[3] Search Order by ID (Linear Search)");
            System.out.println("[4] View Pending Orders (Queue Traversal)");
            System.out.println("[5] View Completed Orders Sorted by Amount (Insertion Sort)");
            System.out.println("[6] View Canteen Menu");
            System.out.println("[7] Exit");
            System.out.println("===============================================================");
            System.out.print("Enter your choice (1-7): ");

            int choice = readIntChoice(1, 7);
            System.out.println();

            switch (choice) {
                case 1 -> handlePlaceOrder();
                case 2 -> handleServeNextOrder();
                case 3 -> handleSearchOrder();
                case 4 -> handleViewPendingOrders();
                case 5 -> handleViewCompletedOrdersSorted();
                case 6 -> handleViewMenu();
                case 7 -> {
                    System.out.println("Thank you for using the Canteen Order Queue Management System!");
                    running = false;
                }
            }

            if (running) {
                System.out.println();
                System.out.print("Press ENTER to return to main menu...");
                scanner.nextLine();
            }
        }
    }

    private static void printHeader() {
        System.out.println("\n===============================================================");
        System.out.println("       CANTEEN ORDER QUEUE MANAGEMENT SYSTEM (DLSL CITE)");
        System.out.println("===============================================================");
        System.out.printf(" Pending Orders: %d | Total Completed: %d%n",
                manager.getPendingOrders().size(), manager.getServedOrders().size());
        System.out.println("---------------------------------------------------------------");
    }

    private static void handlePlaceOrder() {
        System.out.println("--- PLACE NEW ORDER ---");
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine().trim();
        while (customerName.isEmpty()) {
            System.out.print("Customer name cannot be empty. Enter Name: ");
            customerName = scanner.nextLine().trim();
        }

        System.out.println("\n--- CANTEEN MENU ---");
        List<MenuItem> menu = manager.getMenu();
        for (MenuItem item : menu) {
            System.out.println(item);
        }

        System.out.print("\nSelect Menu Item Number (1-" + menu.size() + "): ");
        int itemChoice = readIntChoice(1, menu.size());

        System.out.print("Enter Quantity (1-50): ");
        int quantity = readIntChoice(1, 50);

        Order newOrder = manager.placeOrder(customerName, itemChoice, quantity);
        if (newOrder != null) {
            System.out.println("\n✓ ORDER PLACED SUCCESSFULLY!");
            System.out.println("+-------------------------------------------------------+");
            System.out.printf("| Order ID   : #%-40d |%n", newOrder.getOrderId());
            System.out.printf("| Customer   : %-42s |%n", newOrder.getCustomerName());
            System.out.printf("| Item       : %-42s |%n", newOrder.getItemName());
            System.out.printf("| Quantity   : %-42d |%n", newOrder.getQuantity());
            System.out.printf("| Total Bill : ₱%-41.2f |%n", newOrder.getTotalAmount());
            System.out.printf("| Status     : %-42s |%n", newOrder.getStatus());
            System.out.printf("| Queue Pos  : %-42d |%n", manager.getPendingOrders().size());
            System.out.println("+-------------------------------------------------------+");
        }
    }

    private static void handleServeNextOrder() {
        System.out.println("--- SERVE NEXT ORDER ---");
        Order served = manager.serveNextOrder();
        if (served == null) {
            System.out.println("ℹ The queue is currently empty. No pending orders to serve.");
        } else {
            System.out.println("✓ ORDER HAS BEEN SERVED!");
            System.out.println("+-------------------------------------------------------+");
            System.out.printf("| Order ID   : #%-40d |%n", served.getOrderId());
            System.out.printf("| Customer   : %-42s |%n", served.getCustomerName());
            System.out.printf("| Item       : %-42s |%n", served.getItemName());
            System.out.printf("| Quantity   : %-42d |%n", served.getQuantity());
            System.out.printf("| Total Paid : ₱%-41.2f |%n", served.getTotalAmount());
            System.out.printf("| Status     : %-42s |%n", served.getStatus());
            System.out.println("+-------------------------------------------------------+");
        }
    }

    private static void handleSearchOrder() {
        System.out.println("--- SEARCH ORDER BY ID (LINEAR SEARCH) ---");
        System.out.print("Enter Order ID to search: ");
        int orderId = readInt();

        long startTime = System.nanoTime();
        Order found = manager.linearSearchOrder(orderId);
        long endTime = System.nanoTime();

        if (found == null) {
            System.out.printf("✗ Order #%d was NOT found in active or completed records.%n", orderId);
        } else {
            System.out.println("✓ Order Found!");
            System.out.println("+-------------------------------------------------------+");
            System.out.printf("| Order ID   : #%-40d |%n", found.getOrderId());
            System.out.printf("| Customer   : %-42s |%n", found.getCustomerName());
            System.out.printf("| Item       : %-42s |%n", found.getItemName());
            System.out.printf("| Quantity   : %-42d |%n", found.getQuantity());
            System.out.printf("| Total Bill : ₱%-41.2f |%n", found.getTotalAmount());
            System.out.printf("| Status     : %-42s |%n", found.getStatus());
            System.out.printf("| Ordered At : %-42s |%n", found.getTimestamp());
            System.out.println("+-------------------------------------------------------+");
            System.out.printf("(Linear Search execution time: %.3f µs)%n", (endTime - startTime) / 1000.0);
        }
    }

    private static void handleViewPendingOrders() {
        System.out.println("--- PENDING ORDERS QUEUE (FIFO ORDER) ---");
        List<Order> pending = manager.getPendingOrders();
        if (pending.isEmpty()) {
            System.out.println("No pending orders. Queue is clear!");
            return;
        }

        printOrderTable(pending, true);
    }

    private static void handleViewCompletedOrdersSorted() {
        System.out.println("--- COMPLETED ORDERS SORTED BY TOTAL AMOUNT (INSERTION SORT) ---");
        List<Order> completed = manager.getServedOrders();
        if (completed.isEmpty()) {
            System.out.println("No completed orders yet.");
            return;
        }

        System.out.println("[1] Sort by Highest to Lowest Amount (Descending)");
        System.out.println("[2] Sort by Lowest to Highest Amount (Ascending)");
        System.out.print("Choose sorting order (1-2): ");
        int sortChoice = readIntChoice(1, 2);
        boolean descending = (sortChoice == 1);

        long startTime = System.nanoTime();
        List<Order> sorted = manager.getServedOrdersSortedByAmount(descending);
        long endTime = System.nanoTime();

        printOrderTable(sorted, false);
        System.out.printf("(Insertion Sort completed in %.3f µs on %d records)%n",
                (endTime - startTime) / 1000.0, sorted.size());
    }

    private static void handleViewMenu() {
        System.out.println("--- CANTEEN MENU CATALOG ---");
        System.out.printf("%-5s | %-28s | %-10s%n", "No.", "Meal / Drink", "Price (PHP)");
        System.out.println("-------------------------------------------------");
        for (MenuItem item : manager.getMenu()) {
            System.out.printf("%-5d | %-28s | ₱%-9.2f%n", item.getId(), item.getName(), item.getPrice());
        }
    }

    private static void printOrderTable(List<Order> list, boolean isPending) {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-8s | %-18s | %-24s | %-5s | %-10s | %-8s%n",
                isPending ? "Pos" : "No.", "Order ID", "Customer", "Item", "Qty", "Total", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------");
        int index = 1;
        for (Order o : list) {
            System.out.printf("%-6d | #%-7d | %-18s | %-24s | %-5d | ₱%-9.2f | %-8s%n",
                    index++,
                    o.getOrderId(),
                    truncate(o.getCustomerName(), 18),
                    truncate(o.getItemName(), 24),
                    o.getQuantity(),
                    o.getTotalAmount(),
                    o.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
    }

    private static String truncate(String text, int max) {
        if (text.length() <= max) return text;
        return text.substring(0, max - 3) + "...";
    }

    private static int readIntChoice(int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a valid choice between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number between %d and %d: ", min, max);
            }
        }
    }

    private static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
```

- [ ] **Step 2: Compile the entire project**

Run: `javac -d out src/*.java`
Expected: Successful compilation without errors or warnings.

- [ ] **Step 3: Test CLI with simulated interactive inputs**

Run simulated pipe input:
```bash
printf "1\nJuan Cruz\n1\n2\n1\nMaria Santos\n2\n1\n4\n2\n3\n1001\n5\n1\n7\n" | java -cp out Main
```
Expected: Placed orders, view pending table, served order, linear search found, insertion sort displayed, graceful exit.

- [ ] **Step 4: Commit**

```bash
git add src/Main.java
git commit -m "feat: complete interactive CLI interface for Canteen Order Queue Management System"
```

---

### Task 5: University Course Documentation Template (`GT6B`)

**Files:**
- Create: `docs/finals_section_lastNames.md` (and script/instructions for .docx conversion)

**Interfaces:**
- Produces complete documentation fulfilling De La Salle Lipa CITE Datastrc final project rubrics:
  - 1. Introduction
  - 2. Program Design (ASCII / Mermaid Flowchart & UML)
  - 3. Code Inventory (Control structures, libraries, variables, functions, classes, objects, OOP principles)
  - 4. Concepts of Data Structures and Algorithms (Queue, Linear Search, Insertion Sort)

- [ ] **Step 1: Write documentation matching exact rubric specifications**
- [ ] **Step 2: Commit documentation**

```bash
git add docs/finals_section_lastNames.md
git commit -m "docs: add GT6B final project documentation matching DLSL rubric"
```
