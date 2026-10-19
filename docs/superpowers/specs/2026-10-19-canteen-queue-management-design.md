# Canteen Order Queue Management System — Design Specification

- **Project Title**: Canteen Order Queue Management System
- **Institution**: De La Salle Lipa — College of Information Technology & Engineering (Computer Science Department)
- **Course**: Final Project – Datastrc (Data Structures and Algorithms)
- **Language**: Pure Java (JDK 25 LTS compatible, standard library only, no external dependencies)
- **Date**: 2026-10-19
- **Status**: Approved

---

## 1. Executive Summary & Problem Overview

School canteens face peak-hour operational bottlenecks, disorderly order queues, misplaced order tickets, and difficulties tracking served versus pending customer orders. The **Canteen Order Queue Management System** is a pure Java application that models and streamlines canteen queue transactions using fundamental Data Structures and Algorithms:
1. **FIFO Order Processing**: Guarantees fairness through a pure Java custom Queue data structure.
2. **Instant Order Lookup**: Uses **Linear Search** to find any order across pending and served collections by its Order ID.
3. **Sales Analysis & Audit**: Employs **Insertion Sort** to sort and present served orders by order amount.

---

## 2. Requirements & Feature Matrix

| Feature | Application Function | DSA Applied | Time Complexity |
| :--- | :--- | :--- | :--- |
| **1. Place Order** | Registers a customer, selects menu item and quantity, calculates total price, and assigns auto-increment Order ID. | **Queue — Enqueue** (`enqueue(Order)`) | $O(1)$ |
| **2. Serve Next Order** | Automatically selects and removes the first pending order from the queue, marks it as served, and logs it into served history. | **Queue — Dequeue** (`dequeue()`) | $O(1)$ |
| **3. Search Order** | Searches for an order using its Order ID and displays customer, item, quantity, amount, timestamp, and status (`PENDING` or `SERVED`). | **Linear Search** | $O(n)$ |
| **4. View Orders** | Option A: View pending orders in strict FIFO queue order.<br>Option B: View served orders sorted by total amount (descending or ascending). | **Queue Traversal** (Pending)<br>**Insertion Sort** (Served) | $O(n)$ (Queue)<br>$O(n^2)$ (Insertion Sort) |
| **5. Canteen Menu** | Displays available preset meals and beverages with unit prices. | Array traversal | $O(m)$ |

---

## 3. Architecture & Class Design

The project is structured with clean modular Object-Oriented Programming (OOP) principles:

```text
/Users/macbookairm2/school/DSA/finals/
├── src/
│   ├── MenuItem.java
│   ├── Order.java
│   ├── OrderNode.java
│   ├── OrderQueue.java
│   ├── OrderManager.java
│   └── Main.java
└── docs/
    └── superpowers/specs/
        └── 2026-10-19-canteen-queue-management-design.md
```

### 3.1 Class Responsibilities

#### `MenuItem.java`
- **Purpose**: Encapsulates a food item available in the canteen.
- **Fields**:
  - `private int id`: Menu item identifier (e.g., 1, 2, 3...)
  - `private String name`: Name of the food item (e.g., "Chicken Rice Meal")
  - `private double price`: Unit price in PHP (e.g., 85.00)
- **Methods**: Standard getters and a formatted `toString()` or `display()`.

#### `Order.java`
- **Purpose**: Represents an individual customer transaction.
- **Fields**:
  - `private int orderId`: Unique auto-incrementing ID (starting at 1001)
  - `private String customerName`: Name of the student/faculty customer
  - `private String itemName`: Name of the selected food item
  - `private int quantity`: Number of servings ordered
  - `private double totalAmount`: `unitPrice * quantity`
  - `private String status`: `"PENDING"` or `"SERVED"`
  - `private String timestamp`: Formatted date and time string of placement
- **Methods**: Getters, setters for `status`, and `getFormattedRow()` / `displayDetails()`.

#### `OrderNode.java`
- **Purpose**: The building block for the custom linked Queue. Pure Java class (no external libraries).
- **Fields**:
  - `public Order data`: The order object payload
  - `public OrderNode next`: Reference link to the next node in the queue
- **Constructor**: `public OrderNode(Order data)` initializing `next = null`.

#### `OrderQueue.java`
- **Purpose**: Handcrafted First-In, First-Out (FIFO) Queue implementation.
- **Fields**:
  - `private OrderNode front`: Points to the head of the queue (next order to be served)
  - `private OrderNode rear`: Points to the tail of the queue (most recently enqueued order)
  - `private int size`: Total number of pending orders currently in the queue
- **Methods**:
  - `public void enqueue(Order order)`: Adds a node to the `rear` ($O(1)$).
  - `public Order dequeue()`: Removes and returns the order at `front` ($O(1)$). Returns `null` if empty.
  - `public Order peek()`: Returns order at `front` without removing it ($O(1)$).
  - `public boolean isEmpty()`: Returns `size == 0`.
  - `public int getSize()`: Returns `size`.
  - `public List<Order> getAllOrders()`: Traverses the linked nodes from `front` to `rear` returning orders in FIFO sequence.

#### `OrderManager.java`
- **Purpose**: Core business controller connecting the UI, the queue, and the algorithms.
- **State**:
  - `private OrderQueue pendingQueue`: The active FIFO queue for pending orders.
  - `private List<Order> servedOrders`: Dynamic list of completed orders.
  - `private List<MenuItem> menu`: Catalog of predefined canteen items.
  - `private int nextOrderId`: Counter starting at 1001.
- **Methods**:
  - `public Order placeOrder(String customerName, int menuItemId, int quantity)`: Validates inputs, creates `Order`, enqueues to `pendingQueue`, increments `nextOrderId`, and returns the order.
  - `public Order serveNextOrder()`: Dequeues from `pendingQueue`, updates status to `"SERVED"`, appends to `servedOrders`, and returns the served order.
  - `public Order linearSearchOrder(int orderId)`:
    - Traverses `pendingQueue` nodes comparing `order.getOrderId() == orderId`.
    - If not found, traverses `servedOrders` list comparing `order.getOrderId() == orderId`.
    - Returns matching `Order` or `null`.
  - `public List<Order> getPendingOrders()`: Returns orders directly from `pendingQueue.getAllOrders()`.
  - `public List<Order> getServedOrdersSortedByAmount(boolean descending)`:
    - Creates a copy of `servedOrders`.
    - Applies **Insertion Sort** based on `totalAmount`.
    - Returns sorted list.
  - `public List<MenuItem> getMenu()`: Returns all menu items.

#### `Main.java`
- **Purpose**: Interactive console entry point. Provides text menus, input validation via `Scanner`, ANSI/ASCII formatted banners, and formatted tables for output.
- **Menu Options**:
  1. Place Order (Enqueue)
  2. Serve Next Order (Dequeue)
  3. Search Order by ID (Linear Search)
  4. View Pending Orders (Queue FIFO Traversal)
  5. View Completed Orders Sorted by Amount (Insertion Sort)
  6. View Canteen Menu
  7. Exit

---

## 4. Algorithm Details & Pseudo-code

### 4.1 Custom Queue Operations

```text
Algorithm ENQUEUE(order):
    newNode = new OrderNode(order)
    if rear == null:
        front = newNode
        rear = newNode
    else:
        rear.next = newNode
        rear = newNode
    size = size + 1

Algorithm DEQUEUE():
    if front == null:
        return null // Queue underflow
    servedOrder = front.data
    front = front.next
    if front == null:
        rear = null
    size = size - 1
    return servedOrder
```

### 4.2 Linear Search Algorithm

```text
Algorithm LINEAR_SEARCH(targetId):
    // 1. Search pending queue
    current = front
    while current != null:
        if current.data.orderId == targetId:
            return current.data
        current = current.next

    // 2. Search served orders list
    for i = 0 to servedOrders.length - 1:
        if servedOrders[i].orderId == targetId:
            return servedOrders[i]

    return null // Not found
```

### 4.3 Insertion Sort Algorithm

```text
Algorithm INSERTION_SORT(orders, descending):
    for i = 1 to orders.length - 1:
        keyOrder = orders[i]
        j = i - 1
        
        while j >= 0 and ((descending and orders[j].totalAmount < keyOrder.totalAmount) or
                          (not descending and orders[j].totalAmount > keyOrder.totalAmount)):
            orders[j + 1] = orders[j]
            j = j - 1
            
        orders[j + 1] = keyOrder
```

---

## 5. Error Handling & Input Validation Strategy

1. **Numeric Input Sanitization**: Prevents `InputMismatchException` using scanner wrapper methods (`readInt`, `readPositiveInt`).
2. **Menu Selection Validation**: Validates that selected item ID exists in the menu.
3. **Empty Queue Protection**: Prevents serving or viewing empty queues by displaying informative user messages.
4. **Boundary Checks**: Ensures order quantity is a positive integer between 1 and 50.

---

## 6. Alignment with De La Salle Lipa Rubric

- **Data Structures**: Pure Java custom LinkedList-based Queue (`OrderNode`, `OrderQueue`).
- **Searching Algorithm**: Linear Search traversing both queue and completed list.
- **Sorting Algorithm**: In-place Insertion Sort on completed orders by amount.
- **OOP Concepts Applied**:
  - *Encapsulation*: Private member variables with public accessors/mutators.
  - *Abstraction*: `OrderQueue` abstracts internal pointer manipulations (`front`, `rear`, `next`).
  - *Modularity*: Clear separation between model, data structure, business logic, and presentation.
- **Documentation Ready**: Structure directly supplies all material needed for Section 4 (Introduction, Program Design, Code Inventory, and DSA Concepts) of the final report.
