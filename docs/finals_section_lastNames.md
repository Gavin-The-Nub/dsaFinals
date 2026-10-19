# DE LA SALLE LIPA
## College of Information Technology & Engineering
### Computer Science Department
### Final Project – Datastrc (Data Structures and Algorithms)

**Project Title**: Canteen Order Queue Management System  
**Document Name**: `finals_section_lastNames.docx`  
**Course Code**: Datastrc  
**Instructor**: Arabelle E. Balita  
**Group Members**: [Insert Student Names and Section Here]  
**Date**: October 19, 2026  

---

## 1. Introduction

In high-volume school canteens such as those in De La Salle Lipa, peak operational hours (breakfast, lunch, and class dismissals) frequently encounter operational friction. Traditional manual pen-and-paper ordering or disorganized counter lines lead to customer dissatisfaction, order confusion, prolonged waiting times, and loss of transactional accountability.

The **Canteen Order Queue Management System** is a pure Java terminal-based application designed to bridge computer science theory and real-world operations. By implementing fundamental data structures and algorithmic paradigms, the application automates:
- **Order Placement**: Customers are registered, food items are selected from a catalog, totals are calculated, and unique order tokens are enqueued.
- **Fair FIFO Order Service**: Orders are processed in the strict order they were received using a custom First-In, First-Out (FIFO) Queue.
- **Instant Order Tracking**: Fast linear lookups locate any customer order across pending and completed records using an Order ID.
- **Sales & Auditing Analytics**: Cashiers and managers can review order history sorted by transaction amount via an in-place Insertion Sort algorithm.

---

## 2. Program Design

The system adheres to modular Object-Oriented Programming (OOP) principles, separating concerns across distinct layers:

### 2.1 System Architecture & Flowchart

```
       [ Start Application (Main.java) ]
                      |
                      v
             [ Display Main Menu ]
                      |
        +-------------+-------------+-------------+-------------+
        |             |             |             |             |
        v             v             v             v             v
  [1. Place]    [2. Serve]    [3. Search]   [4. View]     [5. Sort]
    Order         Order         Order        Pending       Served
        |             |             |             |             |
     Enqueue       Dequeue       Linear      Queue FIFO    Insertion
     to Queue    from Queue      Search      Traversal       Sort
        |             |             |             |             |
        +-------------+-------------+-------------+-------------+
                      |
                      v
             [ Continue / Exit ]
```

### 2.2 Class UML Diagram

```
+-----------------------------------+
|             MenuItem              |
+-----------------------------------+
| - id: int                         |
| - name: String                    |
| - price: double                   |
+-----------------------------------+
| + getId(): int                    |
| + getName(): String               |
| + getPrice(): double              |
| + toString(): String              |
+-----------------------------------+
                  ^
                  | references
+-----------------------------------+        +-----------------------------------+
|              Order                |        |             OrderNode             |
+-----------------------------------+        +-----------------------------------+
| - orderId: int                    | <----- | + data: Order                     |
| - customerName: String            |        | + next: OrderNode                 |
| - itemName: String                |        +-----------------------------------+
| - quantity: int                   |                          ^
| - totalAmount: double             |                          | linked chain
| - status: String                  |        +-----------------------------------+
| - timestamp: String               |        |            OrderQueue             |
+-----------------------------------+        +-----------------------------------+
| + getOrderId(): int               |        | - front: OrderNode                |
| + getCustomerName(): String       |        | - rear: OrderNode                 |
| + getItemName(): String           |        | - size: int                       |
| + getQuantity(): int              |        +-----------------------------------+
| + getTotalAmount(): double        |        | + enqueue(Order): void            |
| + getStatus(): String             |        | + dequeue(): Order                |
| + setStatus(String): void         |        | + peek(): Order                   |
| + getTimestamp(): String          |        | + isEmpty(): boolean              |
+-----------------------------------+        | + getSize(): int                  |
                                             | + getAllOrders(): List<Order>     |
                                             +-----------------------------------+
                                                               ^
                                                               | manages
+--------------------------------------------------------------+
|                        OrderManager                          |
+--------------------------------------------------------------+
| - pendingQueue: OrderQueue                                   |
| - servedOrders: List<Order>                                  |
| - menu: List<MenuItem>                                       |
| - nextOrderId: int                                           |
+--------------------------------------------------------------+
| + placeOrder(customerName, menuItemId, quantity): Order      |
| + serveNextOrder(): Order                                    |
| + linearSearchOrder(orderId: int): Order                     |
| + getPendingOrders(): List<Order>                            |
| + getServedOrdersSortedByAmount(descending: boolean): List   |
+--------------------------------------------------------------+
                               ^
                               | drives
+--------------------------------------------------------------+
|                            Main                              |
+--------------------------------------------------------------+
| + main(String[] args): void                                  |
| - handlePlaceOrder(): void                                   |
| - handleServeNextOrder(): void                               |
| - handleSearchOrder(): void                                  |
| - handleViewPendingOrders(): void                            |
| - handleViewCompletedOrdersSorted(): void                    |
+--------------------------------------------------------------+
```

---

## 3. Code Inventory

### 3.1 Control Structures
| Control Structure | Aspect of the Problem Used | Description |
| :--- | :--- | :--- |
| `while (running)` | Main application loop in `Main.java` | Keeps the console interactive until the user explicitly selects option 7 (Exit). |
| `switch (choice)` | Menu routing in `Main.java` | Routes user menu inputs (1 to 7) to their respective transaction handlers. |
| `while (current != null)` | Queue traversal in `OrderQueue.java` and `OrderManager.java` | Iterates across linked nodes from `front` to `rear` without dequeuing. |
| `while (j >= 0 && ...)` | Inner loop of Insertion Sort in `OrderManager.java` | Shifts elements to accommodate the current key element in sorted order. |
| `for (int i = 1; i < n; i++)` | Outer loop of Insertion Sort | Progressively partitions the completed orders list into sorted and unsorted segments. |
| `try-catch` | Input validation (`readIntChoice`, `readInt`) | Catches `NumberFormatException` to prevent program crashes when users enter letters instead of numbers. |
| `if-else` | Boundary checks & Queue condition handling | Guards against null pointers, empty queue dequeues, and invalid item selections. |

### 3.2 Libraries Used
- `java.util.Scanner`: Reads user input from standard input (`System.in`).
- `java.util.List` / `java.util.ArrayList`: Dynamic array structure used exclusively for the catalog menu and completed order history.
- `java.time.LocalDateTime` & `java.time.format.DateTimeFormatter`: Generates human-readable timestamps for placed orders.

### 3.3 Classes, Objects, and Methods
1. **`MenuItem`**:
   - Variables: `id` (int), `name` (String), `price` (double).
   - Methods: `getId()`, `getName()`, `getPrice()`, `toString()`.
2. **`Order`**:
   - Variables: `orderId` (int), `customerName` (String), `itemName` (String), `quantity` (int), `totalAmount` (double), `status` (String), `timestamp` (String).
   - Methods: Getters, `setStatus(String)`.
3. **`OrderNode`**:
   - Variables: `data` (`Order`), `next` (`OrderNode`).
4. **`OrderQueue`**:
   - Variables: `front` (`OrderNode`), `rear` (`OrderNode`), `size` (int).
   - Methods: `enqueue(Order)`, `dequeue()`, `peek()`, `isEmpty()`, `getSize()`, `getAllOrders()`.
5. **`OrderManager`**:
   - Variables: `pendingQueue`, `servedOrders`, `menu`, `nextOrderId`, `timeFormatter`.
   - Methods: `placeOrder()`, `serveNextOrder()`, `linearSearchOrder()`, `getServedOrdersSortedByAmount()`.
6. **`Main`**:
   - Controller class hosting the CLI event loop and input sanitation helpers.

### 3.4 Principles of OOP Applied
- **Classes and Objects**: All system entities (orders, menu items, queue nodes) are modelled as first-class Java classes instantiated into stateful objects at runtime.
- **Encapsulation**: Critical fields are declared `private` or `final` (e.g., `OrderQueue.front`, `Order.orderId`, `MenuItem.price`), preventing direct external mutation and exposing safe accessor methods.
- **Abstraction**: The complexity of managing pointers (`front`, `rear`, `next`) is hidden inside `OrderQueue`. Callers merely interact with clean abstractions like `enqueue()` and `dequeue()`.
- **Single Responsibility Principle (SRP)**: Each class has one distinct responsibility (Model, Data Structure, Business Logic, Presentation).

---

## 4. Concepts of Data Structures and Algorithms

### 4.1 Queue Data Structure (FIFO)
- **Concept**: A Queue is a linear collection governed by the First-In, First-Out (FIFO) principle: the first element inserted is the first one removed.
- **Implementation in Code**:
  - Implemented from scratch via `OrderNode` and `OrderQueue`.
  - Maintains two pointer references: `front` (head) and `rear` (tail).
  - **Enqueue ($O(1)$)**: When an order is placed, a new `OrderNode` is created. If `rear == null`, both `front` and `rear` point to the new node. Otherwise, `rear.next = newNode` and `rear = newNode`.
  - **Dequeue ($O(1)$)**: When the kitchen serves the next order, `front` advances to `front.next`. If `front` becomes null, `rear` is also reset to null.
- **Real-World Value**: Guarantees that students and faculty are served fairly in the exact order their tickets were registered.

### 4.2 Linear Search Algorithm
- **Concept**: Sequentially checks each record in a collection from beginning to end until a match is found or all records have been inspected.
- **Implementation in Code**:
  - Function: `OrderManager.linearSearchOrder(int orderId)`.
  - Step 1: Traverses the active `pendingQueue` linked chain comparing `order.getOrderId() == orderId`.
  - Step 2: If not found in the queue, iterates through the `servedOrders` list.
  - Returns the matching `Order` object or `null`.
- **Complexity**:
  - **Best Case**: $O(1)$ (matching order is at the very front of the queue).
  - **Worst / Average Case**: $O(n)$ where $n$ is total orders.

### 4.3 Insertion Sort Algorithm
- **Concept**: An efficient, in-place comparison sort that builds the final sorted array one element at a time by repeatedly taking the next element and inserting it into its correct position relative to the already-sorted prefix.
- **Implementation in Code**:
  - Function: `OrderManager.getServedOrdersSortedByAmount(boolean descending)`.
  - Creates a copy of `servedOrders`.
  - Loops from index 1 to $N-1$, extracting `keyOrder = list.get(i)`.
  - Shifts preceding elements while their `totalAmount` is smaller (descending) or larger (ascending) than `keyOrder`.
  - Inserts `keyOrder` at position `j + 1`.
- **Complexity**:
  - **Best Case**: $O(n)$ (already sorted).
  - **Worst / Average Case**: $O(n^2)$.
- **Real-World Value**: Enables canteen managers to audit top-grossing meal transactions and financial sales reports.

---

## 5. Verification & Testing

The program includes automated unit tests verifying correctness without external dependencies:
1. `test/ModelsTest.java`: Verifies property integrity of `MenuItem` and `Order`.
2. `test/QueueTest.java`: Verifies empty conditions, FIFO sequence preservation, pointer updates, and boundary conditions.
3. `test/AlgorithmsTest.java`: Verifies enqueue/dequeue logic, multi-container Linear Search, and both ascending/descending Insertion Sort.

To compile and run:
```bash
# Compile
javac -d out src/*.java

# Run Application
java -cp out Main
```
