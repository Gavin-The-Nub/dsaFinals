# CODE INVENTORY

This inventory covers all six source files of the Canteen Order Queue Management System: **Main.java**, **OrderManager.java**, **OrderQueue.java**, **OrderNode.java**, **Order.java** and **MenuItem.java**. Line numbers refer to the current source files.

---

## 1. Control Structures

The program uses four kinds of control structures:
- **selection**, which decides what to do;
- **repetition**, which repeats work;
- **exception handling**, which recovers from errors;
- **jump statements**, which leave a method or the program early.

### 1.1 Selection Structures

| # | Control structure | Location | Aspect of the problem where it is used |
|---|---|---|---|
| 1 | `switch` (arrow form, `case 1 -> …`) | Main.java:46–59, `main()` | **Menu routing.** Sends the user's choice (1–7) to the matching feature. There is no `default` case because `readIntChoice(1, 7)` only ever returns 1 to 7. |
| 2 | `if` | Main.java:54, `main()` (case 7) | **Exit confirmation.** The program only ends if the user answers Y to "Are you sure you want to exit?". It then sets `running = false`. |
| 3 | `if` | Main.java:61, `main()` | **Pause between screens.** Shows "Press ENTER to return to main menu..." only while the program is still running (not after exiting). |
| 4 | `if` | Main.java:74, `enableUtf8Output()` | **Peso sign display.** Runs the console code-page command only on Windows, the system that needs it. |
| 5 | `if-else` (nested in a `while`) | Main.java:105–109, `handlePlaceOrder()` | **Customer name validation.** Chooses the error message: "cannot be empty" for a blank name, or "must contain letters" for a name made only of numbers or symbols. |
| 6 | `if` | Main.java:126, `handlePlaceOrder()` | **Order receipt.** Prints the receipt only if an order was actually created (`newOrder != null`). |
| 7 | `if` (guard with early `return`) | Main.java:148, `handleServeNextOrder()` | **Empty queue.** If no order is waiting, shows "The queue is empty. No pending orders to serve." and stops. |
| 8 | `if` (guard with early `return`) | Main.java:156, `handleServeNextOrder()` | **Serve confirmation.** If the user answers N, shows "Serve cancelled. The queue is unchanged." and stops without dequeuing. |
| 9 | `if-else` | Main.java:187–201, `handleSearchOrder()` | **Search result.** Shows "Order not found", or the order's details and the search time. |
| 10 | `if` (guard with early `return`) | Main.java:211, `handleViewPendingOrders()` | **Empty queue.** Shows a message instead of an empty table. |
| 11 | `if` (guard with early `return`) | Main.java:226, `handleViewCompletedOrdersSorted()` | **No served orders.** Shows "No served orders yet." instead of sorting an empty list. |
| 12 | Conditional (ternary) operator `? :` | Main.java:258, `printOrderTable()` | **Table heading.** The first column is titled "Pos" (queue position) for pending orders and "No." for completed orders. |
| 13 | Two `if` statements in a row | Main.java:284–285, `truncate()` | **Fitting text in table columns.** Returns "" for null text and returns short text unchanged; otherwise long text is cut and ends with "...". |
| 14 | `if` | Main.java:293, `readLine()` | **Closed input.** If there is no more input (Ctrl+Z or Ctrl+D), the program ends cleanly instead of crashing. |
| 15 | Nested `if` | Main.java:307 and 310, `readIntChoice()` | **Number validation.** The outer `if` accepts digits only; the inner `if` checks the value is between `min` and `max`. |
| 16 | `if-else` | Main.java:317–321, `readIntChoice()` | **Error message.** Shows "Enter a valid number" for an Order ID (no upper limit) or "Enter a number from X to Y" for a range. |
| 17 | Two `if` statements in a row | Main.java:330–331, `confirm()` | **Y/N answers.** Y returns `true` and N returns `false` (upper or lower case). Anything else falls through to an error message. |
| 18 | `if` | OrderManager.java:51, `getMenuItemById()` | **Menu lookup.** Returns the item whose ID matches the user's choice. |
| 19 | `if` with `\|\|` conditions | OrderManager.java:74–77, `placeOrder()` | **Order validation.** Rejects the order if the name has no letter, the item does not exist, or the quantity is outside 1–10. |
| 20 | `if` | OrderManager.java:105, `serveNextOrder()` | **Serving.** Marks the order SERVED and saves it to the history only if an order was actually dequeued. |
| 21 | `if` (two, one per list) | OrderManager.java:125 and 132, `linearSearchOrder()` | **Linear search.** Checks whether the current order's ID equals the ID being searched. |
| 22 | `if-else` | OrderManager.java:165–177, `getServedOrdersSortedByAmount()` | **Sort direction.** Sorts highest-to-lowest (`descending = true`) or lowest-to-highest. |
| 23 | `if-else` | OrderQueue.java:29–35, `enqueue()` | **Queue insertion.** An empty queue makes the new node both front and rear; otherwise it is linked after the current rear. |
| 24 | `if` (guard) | OrderQueue.java:44, `dequeue()` | **Empty queue.** Returns `null` instead of reading from an empty queue. |
| 25 | `if` | OrderQueue.java:49, `dequeue()` | **Queue becomes empty.** After removing the last order, `rear` is also reset to `null`. |
| 26 | `if` (guard) | OrderQueue.java:61, `peek()` | **Empty queue.** Returns `null` when there is no front order. |

**Short-circuit logical operators** (`&&`, `||`) also control which checks run:
- `customerName != null && …` (OrderManager.java:63) stops a `NullPointerException` before `matches()` runs.
- `j >= 0 && list.get(j)…` (OrderManager.java:167, 173) stops the sort from reading index −1.
- `val >= min && val <= max` (Main.java:310) is the range check.

### 1.2 Repetition Structures (Loops)

| # | Control structure | Location | Aspect of the problem where it is used |
|---|---|---|---|
| 27 | `while (running)` | Main.java:31, `main()` | **Main program loop.** Shows the header and menu again after every feature, until the user confirms Exit. It is a flag-controlled loop: `running = false` ends it. |
| 28 | `while (!isValidCustomerName(...))` | Main.java:104, `handlePlaceOrder()` | **Input validation loop.** Asks for the customer name again until it contains at least one letter. |
| 29 | `while (true)` | Main.java:305, `readIntChoice()` | **Input validation loop.** Repeats until a valid whole number in range is typed. `return val` leaves the loop. |
| 30 | `while (true)` | Main.java:327, `confirm()` | **Input validation loop.** Repeats until the user answers Y or N. |
| 31 | `for-each` | Main.java:115, `handlePlaceOrder()` | **Menu display.** Prints each menu item so the customer can choose. |
| 32 | `for-each` | Main.java:249, `handleViewMenu()` | **Menu catalog.** Prints each item's number, name and price as a table row. |
| 33 | `for-each` with a counter (`index++`) | Main.java:261, `printOrderTable()` | **Order tables.** Prints one numbered row per order for the pending and completed tables. |
| 34 | `for-each` | OrderManager.java:50, `getMenuItemById()` | **Linear search** through the menu. |
| 35 | `for-each` (two, one after the other) | OrderManager.java:124 and 131, `linearSearchOrder()` | **Linear search**, first through the pending orders, then through the served orders. |
| 36 | `for` (counter `i`) | OrderManager.java:161, `getServedOrdersSortedByAmount()` | **Insertion sort, outer loop.** Takes each order from index 1 to n − 1 as the key. |
| 37 | `while` (nested in the `for`, two versions) | OrderManager.java:167 and 173, `getServedOrdersSortedByAmount()` | **Insertion sort, inner loop.** Shifts orders one place right until the key's spot is found. |
| 38 | `while (current != null)` | OrderQueue.java:89, `getAllOrders()` | **Linked list traversal.** Follows the `next` pointers from front to rear to collect every pending order. |

### 1.3 Exception Handling

| # | Control structure | Location | Aspect of the problem where it is used |
|---|---|---|---|
| 39 | `try-catch (Exception e)` | Main.java:75–82, `enableUtf8Output()` | **Console setup.** If switching the Windows console to UTF-8 fails, the program keeps running; only the peso sign might display incorrectly. |
| 40 | `try-catch (NumberFormatException e)` | Main.java:308–315, `readIntChoice()` | **Very large numbers.** Digits too large for an `int` (for example 99999999999) are treated as invalid input instead of crashing. Letters never reach `parseInt` because of the `[0-9]+` check. |

### 1.4 Jump Statements

| # | Control structure | Location | Aspect of the problem where it is used |
|---|---|---|---|
| 41 | Early `return` (guard clauses) | Main.java:150, 158, 213, 228 | **Stops a feature early** when there is nothing to do (empty queue, cancelled serve, no served orders). |
| 42 | `return` inside a loop | OrderManager.java:52, 126, 133; Main.java:311, 330, 331 | **Ends a search or validation loop** as soon as the answer is known (match found, valid number, Y or N). |
| 43 | `System.exit(0)` | Main.java:295, `readLine()` | **Ends the program** when the input stream is closed. |

The program uses **no** `do-while`, `break`, `continue`, `throw`, `finally` or recursion.

---

## 2. Libraries Used

The project uses only the **Java Standard Library**; there are no external libraries. The queue is hand-made: `java.util.Queue` and `java.util.LinkedList` are not used.

### 2.1 Imported Libraries

| Library (package.Class) | Used in | Purpose in the program |
|---|---|---|
| `java.util.Scanner` | Main.java | Reads the user's keyboard input line by line (`hasNextLine()`, `nextLine()`). |
| `java.util.List` | Main.java, OrderManager.java, OrderQueue.java | The list type used for the menu, the served orders history, and returned lists (`List<Order>`, `List<MenuItem>`). |
| `java.util.ArrayList` | OrderManager.java, OrderQueue.java | The dynamic array behind every list: `add()`, `get()`, `set()`, `size()`, plus copies with `new ArrayList<>(...)`. |
| `java.time.LocalDateTime` | OrderManager.java | `LocalDateTime.now()` gets the date and time an order is placed. |
| `java.time.format.DateTimeFormatter` | OrderManager.java | Formats the timestamp as `yyyy-MM-dd HH:mm:ss`. |
| `java.io.PrintStream` | Main.java | A new console output stream that writes UTF-8, so the peso sign (₱) prints correctly. |
| `java.io.FileOutputStream` | Main.java | Connects that output stream to the console. |
| `java.io.FileDescriptor` | Main.java | `FileDescriptor.out` refers to the console's standard output. |
| `java.nio.charset.StandardCharsets` | Main.java | `StandardCharsets.UTF_8`, the character encoding for the output stream. |

### 2.2 Built-in `java.lang` Classes (no import needed)

| Class | Members used | Purpose in the program |
|---|---|---|
| `System` | `out.print/println/printf`, `in`, `getProperty("os.name")`, `nanoTime()`, `setOut()`, `exit()` | Console output and input, detecting Windows, timing the search and sort, replacing the output stream, ending the program. |
| `String` | `format()`, `valueOf()`, `trim()`, `isEmpty()`, `matches()`, `equalsIgnoreCase()`, `substring()`, `length()`, `startsWith()`, `repeat()` | Formatting money and receipts, cleaning input, regex checks, Y/N answers, shortening long text, and building the receipt border. |
| `Integer` | `parseInt()`, `MAX_VALUE` | Turning typed digits into a number; `MAX_VALUE` means "no upper limit" for the Order ID search. |
| `ProcessBuilder`, `Process` | `ProcessBuilder.Redirect.INHERIT`, `start()`, `waitFor()` | Runs the Windows command `chcp 65001` to switch the console to UTF-8. |
| `Exception`, `NumberFormatException` | `catch` blocks | Error handling (see 1.3). |
| `Object` | `toString()` (overridden) | The automatic parent class of every class in the project. |
| `@Override` | annotation | Marks `toString()` in MenuItem and Order as overriding `Object.toString()`. |

**Regular expressions used:**
- `".*\\p{L}.*"` (OrderManager.java:63): the text must contain at least one letter, in any language.
- `"[0-9]+"` (Main.java:307): the text must be digits only.

---

## 3. Variables

### 3.1 Fields (class-level variables)

| Class | Field | Type | Modifiers | Description |
|---|---|---|---|---|
| Main | `scanner` | Scanner | private static final | Reads the user's input from the keyboard. |
| Main | `manager` | OrderManager | private static final | The single OrderManager that every menu option uses. |
| Main | `RECEIPT_VALUE_WIDTH` | int (= 36) | private static final | Width of the value column in receipt boxes. |
| Main | `RECEIPT_BORDER` | String | private static final | The `+-----+` border line printed above and below each receipt. |
| OrderManager | `MIN_QUANTITY` | int (= 1) | public static final | Smallest quantity allowed per order. |
| OrderManager | `MAX_QUANTITY` | int (= 10) | public static final | Largest quantity allowed per order. |
| OrderManager | `pendingQueue` | OrderQueue | private final | FIFO queue of orders waiting to be served. |
| OrderManager | `servedOrders` | List\<Order\> | private final | History of served orders, in the order they were served. |
| OrderManager | `menu` | List\<MenuItem\> | private final | The 7 canteen menu items. |
| OrderManager | `nextOrderId` | int | private | ID for the next order; starts at 1001 and goes up by 1. |
| OrderManager | `timeFormatter` | DateTimeFormatter | private final | Formats order timestamps as `yyyy-MM-dd HH:mm:ss`. |
| OrderQueue | `front` | OrderNode | private | First node in the queue (next order to serve). |
| OrderQueue | `rear` | OrderNode | private | Last node in the queue (newest order). |
| OrderQueue | `size` | int | private | Number of orders in the queue. |
| OrderNode | `data` | Order | public | The order stored in this node. |
| OrderNode | `next` | OrderNode | public | The next node in the chain (`null` for the last node). |
| Order | `orderId` | int | private final | Unique order number (1001, 1002, …). |
| Order | `customerName` | String | private final | Name of the customer. |
| Order | `itemName` | String | private final | Name of the ordered menu item. |
| Order | `quantity` | int | private final | Number of servings (1–10). |
| Order | `totalAmount` | double | private final | Price × quantity, in pesos. |
| Order | `status` | String | private | "PENDING" when placed, "SERVED" when served. |
| Order | `timestamp` | String | private final | Date and time the order was placed. |
| MenuItem | `id` | int | private final | Item number shown on the menu (1–7). |
| MenuItem | `name` | String | private final | Name of the meal or drink. |
| MenuItem | `price` | double | private final | Price in pesos. |

### 3.2 Parameters and Local Variables

| Method | Parameters | Local variables | Description |
|---|---|---|---|
| `Main.main` | `args: String[]` | `running: boolean`, `choice: int` | `running` keeps the main loop going; `choice` is the menu option typed. |
| `Main.enableUtf8Output` | — | `e: Exception` (catch) | Holds the error if the code-page command fails (ignored on purpose). |
| `Main.handlePlaceOrder` | — | `customerName: String`, `menu: List<MenuItem>`, `item: MenuItem` (loop), `itemChoice: int`, `quantity: int`, `newOrder: Order` | Input collected for a new order and the order that was created. |
| `Main.handleServeNextOrder` | — | `next: Order`, `served: Order` | The order at the front (shown for confirmation) and the order that was served. |
| `Main.handleSearchOrder` | — | `orderId: int`, `startTime: long`, `endTime: long`, `found: Order`, `elapsedMicroseconds: double` | Search target, timing values, and result. |
| `Main.handleViewPendingOrders` | — | `pending: List<Order>` | Pending orders in FIFO order. |
| `Main.handleViewCompletedOrdersSorted` | — | `completed: List<Order>`, `startTime: long`, `endTime: long`, `sorted: List<Order>`, `elapsedMicroseconds: double` | Served orders, timing values, and the sorted list. |
| `Main.handleViewMenu` | — | `item: MenuItem` (loop) | Each menu item being printed. |
| `Main.printOrderTable` | `list: List<Order>`, `isPending: boolean` | `index: int`, `o: Order` (loop) | Orders to print, which table it is, and the row counter. |
| `Main.printReceiptRow` | `label: String`, `value: String` | — | One "label : value" row of a receipt. |
| `Main.peso` | `amount: double` | — | Amount to format as "₱0.00". |
| `Main.truncate` | `text: String`, `max: int` | — | Text to shorten and the column width. |
| `Main.readIntChoice` | `min: int`, `max: int` | `line: String`, `val: int`, `e: NumberFormatException` (catch) | Allowed range, typed text, and the parsed number. |
| `Main.confirm` | `question: String` | `answer: String` | The Y/N question and the user's answer. |
| `OrderManager.getMenuItemById` | `id: int` | `item: MenuItem` (loop) | Item number to find, and each item checked. |
| `OrderManager.isValidCustomerName` | `customerName: String` | — | Name to check. |
| `OrderManager.placeOrder` | `customerName: String`, `menuItemId: int`, `quantity: int` | `item: MenuItem`, `totalAmount: double`, `timestamp: String`, `newOrder: Order` | Order input, the chosen item, the computed total, and the new order. |
| `OrderManager.serveNextOrder` | — | `order: Order` | The order taken from the front of the queue. |
| `OrderManager.linearSearchOrder` | `orderId: int` | `pendingList: List<Order>`, `order: Order` (loop variable in each of the two loops) | Target ID, pending orders, and each order compared. |
| `OrderManager.getServedOrdersSortedByAmount` | `descending: boolean` | `list: List<Order>`, `n: int`, `i: int`, `keyOrder: Order`, `j: int` | Sort direction, the copy being sorted, its size, the outer index, the key, and the inner index. |
| `OrderQueue.enqueue` | `order: Order` | `newNode: OrderNode` | Order to add and the node that wraps it. |
| `OrderQueue.dequeue` | — | `removedOrder: Order` | The front order being removed and returned. |
| `OrderQueue.getAllOrders` | — | `orders: List<Order>`, `current: OrderNode` | Result list and the node currently being visited. |
| `Order` constructor | `orderId`, `customerName`, `itemName`, `quantity`, `totalAmount`, `status`, `timestamp` | — | Values copied into the new order's fields. |
| `Order.setStatus` | `status: String` | — | New status ("SERVED"). |
| `MenuItem` constructor | `id: int`, `name: String`, `price: double` | — | Values copied into the new item's fields. |
| `OrderNode` constructor | `data: Order` | — | The order the node will hold. |

Methods not listed here have no parameters and no local variables.

---

## 4. Functions (Methods)

### 4.1 Main — console user interface (all methods are `static`)

| Method | Description | Called from |
|---|---|---|
| `main(String[] args): void` | Program entry point. Sets up UTF-8 output, then loops: header → menu → read choice → run feature → pause, until the user exits. | Java runtime |
| `enableUtf8Output(): void` | On Windows, runs `chcp 65001` and replaces `System.out` with a UTF-8 stream so "₱" displays correctly. | Main.java:29 |
| `printHeader(): void` | Prints the title and the current number of pending and served orders. | Main.java:32 |
| `handlePlaceOrder(): void` | Option 1. Reads and validates the name, shows the menu, reads item and quantity, places the order, and prints the receipt. | Main.java:47 |
| `handleServeNextOrder(): void` | Option 2. Shows the next order, asks for confirmation, serves it, and prints the receipt. | Main.java:48 |
| `handleSearchOrder(): void` | Option 3. Reads an Order ID, runs linear search, and prints the result with the search time. | Main.java:49 |
| `handleViewPendingOrders(): void` | Option 4. Prints the pending orders in FIFO order. | Main.java:50 |
| `handleViewCompletedOrdersSorted(): void` | Option 5. Sorts the served orders by amount (highest first) and prints them with the sort time. | Main.java:51 |
| `handleViewMenu(): void` | Option 6. Prints the menu as a table. | Main.java:52 |
| `printOrderTable(List<Order>, boolean): void` | Prints a table of orders; the first column is "Pos" for pending orders or "No." for completed orders. | Main.java:216, 236 |
| `printReceiptRow(String, String): void` | Prints one aligned "label : value" row inside a receipt box. | 21 calls (Main.java:129–136, 164–169, 192–198) |
| `peso(double): String` | Formats an amount as "₱0.00". | Main.java:133, 168, 196 |
| `truncate(String, int): String` | Shortens text that is too long for its column and adds "...". | Main.java:265, 266, 276 |
| `readLine(): String` | Reads one line; ends the program cleanly if input is closed. | Main.java:64, 103, 110, 306, 329 |
| `readIntChoice(int, int): int` | Reads a whole number between `min` and `max`, asking again until the input is valid. | Main.java:43, 120, 123, 180 |
| `confirm(String): boolean` | Asks a Y/N question until the answer is Y or N. | Main.java:54, 156 |

### 4.2 OrderManager — business logic and algorithms

| Method | Description | Called from |
|---|---|---|
| `OrderManager()` (constructor) | Creates the queue and both lists, sets the first ID to 1001, sets the time format, and loads the menu. | Main.java:22 |
| `initializeMenu(): void` (private) | Adds the 7 menu items with their prices. | OrderManager.java:29 |
| `getMenu(): List<MenuItem>` | Returns the menu list. | Main.java:114, 249 |
| `getMenuItemById(int): MenuItem` | **Linear search** for a menu item by its number; `null` if not found. | OrderManager.java:73 |
| `isValidCustomerName(String): boolean` (static) | `true` if the name contains at least one letter. | Main.java:104; OrderManager.java:74 |
| `placeOrder(String, int, int): Order` | Validates input, computes the total, creates the order, and **enqueues** it. Returns `null` if the input is invalid. | Main.java:125 |
| `peekNextOrder(): Order` | Returns the front order without removing it (**peek**). | Main.java:147 |
| `serveNextOrder(): Order` | **Dequeues** the front order, sets it to SERVED, and adds it to `servedOrders`. | Main.java:161 |
| `linearSearchOrder(int): Order` | **Linear search** by Order ID, first in the pending queue, then in the served list. | Main.java:183 |
| `getPendingOrders(): List<Order>` | Returns the pending orders in FIFO order (queue traversal). | Main.java:92, 135, 210 |
| `getServedOrdersSortedByAmount(boolean): List<Order>` | **Insertion sort** of a copy of the served orders by total amount. | Main.java:232 |
| `getServedOrders(): List<Order>` | Returns a copy of the served orders history. | Main.java:92, 225 |

### 4.3 OrderQueue — custom FIFO queue

| Method | Description | Called from |
|---|---|---|
| `OrderQueue()` (constructor) | Creates an empty queue: front and rear are `null`, size is 0. | OrderManager.java:24 |
| `enqueue(Order): void` | Adds an order at the rear. O(1). | OrderManager.java:84 |
| `dequeue(): Order` | Removes and returns the front order, or `null` if empty. O(1). | OrderManager.java:104 |
| `peek(): Order` | Returns the front order without removing it. O(1). | OrderManager.java:93 |
| `isEmpty(): boolean` | `true` if the queue has no orders. O(1). | OrderQueue.java:44, 61 |
| `getSize(): int` | Returns the number of orders. O(1). | Not called in the current program |
| `getAllOrders(): List<Order>` | Traverses the linked list front to rear and returns the orders. O(n). | OrderManager.java:123, 145 |

### 4.4 OrderNode, Order and MenuItem

| Method | Description | Called from |
|---|---|---|
| `OrderNode(Order)` (constructor) | Creates a node holding an order, with `next = null`. | OrderQueue.java:28 |
| `Order(...)` (constructor, 7 parameters) | Creates an order with all its details. | OrderManager.java:83 |
| `getOrderId()`, `getCustomerName()`, `getItemName()`, `getQuantity()`, `getTotalAmount()`, `getStatus()`, `getTimestamp()` | Getters that return each field of an order. | Receipts and tables in Main; `getOrderId()` in linear search; `getTotalAmount()` in insertion sort |
| `setStatus(String): void` | Changes the order's status; the only field that can change. | OrderManager.java:106 |
| `Order.toString(): String` | One-line summary of the order (overrides `Object.toString()`). | Not called in the current program |
| `MenuItem(int, String, double)` (constructor) | Creates a menu item. | OrderManager.java:36–42 |
| `getId()`, `getName()`, `getPrice()` | Getters for the item's number, name and price. | Main.java:250; OrderManager.java:51, 80, 83 |
| `MenuItem.toString(): String` | Formats the item as "[1] Chicken Rice Meal - ₱85.00" (overrides `Object.toString()`). | Main.java:116 (called automatically by `"  " + item`) |

---

## 5. Classes

| Class | File | Role | Description |
|---|---|---|---|
| `Main` | Main.java | User interface | Runs the console program: shows the menu, reads and validates input, calls OrderManager, and prints receipts and tables. |
| `OrderManager` | OrderManager.java | Business logic (controller) | Holds the pending queue, served history and menu, and runs the algorithms: enqueue, dequeue, linear search, and insertion sort. |
| `OrderQueue` | OrderQueue.java | Data structure | A hand-made FIFO queue built as a singly linked list, with `front`, `rear` and `size`. |
| `OrderNode` | OrderNode.java | Data structure | One node of the linked list: an order plus a pointer to the next node. |
| `Order` | Order.java | Data (model) | A customer's order: ID, name, item, quantity, total, status, timestamp. |
| `MenuItem` | MenuItem.java | Data (model) | One item on the canteen menu: number, name, price. |

---

## 6. Objects

| Object | Class | Created at | How many | Description |
|---|---|---|---|---|
| `scanner` | Scanner | Main.java:21 | 1 | Reads keyboard input for the whole program. |
| `manager` | OrderManager | Main.java:22 | 1 | The single controller used by every menu option. |
| `pendingQueue` | OrderQueue | OrderManager.java:24 | 1 | Holds the orders waiting to be served. |
| `servedOrders` | ArrayList\<Order\> | OrderManager.java:25 | 1 | Grows by one each time an order is served. |
| `menu` | ArrayList\<MenuItem\> | OrderManager.java:26 | 1 | Holds the 7 menu items. |
| Menu items | MenuItem | OrderManager.java:36–42 | 7 | Chicken Rice Meal (₱85), Burger Steak w/ Rice (₱75), Pork Tonkatsu Meal (₱90), Spaghetti w/ Garlic Bread (₱65), Crispy French Fries (₱40), Canteen Iced Tea (₱25), Bottled Mineral Water (₱15). |
| `timeFormatter` | DateTimeFormatter | OrderManager.java:28 (`ofPattern`) | 1 | Formats timestamps. |
| `newOrder` | Order | OrderManager.java:83 | 1 per placed order | Each order placed (IDs 1001, 1002, …). The same object moves from the queue to the served history. |
| `newNode` | OrderNode | OrderQueue.java:28 | 1 per enqueue | Wraps an order inside the queue; dropped (garbage-collected) when the order is dequeued. |
| `list` | ArrayList\<Order\> | OrderManager.java:158 | 1 per sort | The copy that insertion sort rearranges. |
| Served history copy | ArrayList\<Order\> | OrderManager.java:185 | 1 per call | Safe copy returned by `getServedOrders()`. |
| `orders` | ArrayList\<Order\> | OrderQueue.java:87 | 1 per traversal | List of pending orders returned by `getAllOrders()`. |
| Timestamp | LocalDateTime | OrderManager.java:81 (`now()`) | 1 per placed order | Current date and time, converted to text immediately. |
| Console command | ProcessBuilder, Process | Main.java:76–79 | 1 (Windows only) | Runs `chcp 65001` once at startup. |
| UTF-8 output stream | PrintStream, FileOutputStream | Main.java:84 | 1 each | Replaces `System.out` so "₱" prints correctly. |

---

## 7. Principles of Object-Oriented Programming

| Principle | Used? | Where it appears |
|---|---|---|
| Classes and Objects | Yes | 6 classes; objects listed in Section 6 |
| Encapsulation | Yes | Private fields with getters in Order, MenuItem, OrderQueue and OrderManager; `getServedOrders()` returns a copy |
| Abstraction | Yes | `enqueue()`/`dequeue()` hide pointer handling; OrderManager hides the algorithms behind simple method names |
| Inheritance | Only from `Object` (no `extends` in the code) | Every class automatically inherits from `java.lang.Object` |
| Polymorphism | Yes, through overriding | `@Override toString()` in MenuItem and Order; dynamic call at Main.java:116 |
| Composition and Aggregation ("has-a") | Yes | Main has an OrderManager; OrderManager has a queue, a menu and a history; OrderQueue has nodes |
| Static (class-level) members | Yes | All of Main; `MIN_QUANTITY`, `MAX_QUANTITY`, `isValidCustomerName()` |
| Interfaces, abstract classes, method overloading | No | Not needed for this program |

**Classes and Objects.** The system is divided into six classes, each a blueprint for one part of the problem. When the program runs, objects are created from them:
- one **OrderManager**;
- one **OrderQueue**;
- seven **MenuItem** objects;
- one **Order** object for every order placed;
- one **OrderNode** for every order waiting in the queue.

Each object keeps its own data. For example, every Order has its own ID, customer name and status.

**Encapsulation.** Fields are declared **private**, so other classes cannot change them directly; they can only use the public methods.
- **Order** and **MenuItem** expose their data only through getters. Most of their fields are also **final**, so they cannot change after the object is created.
- The only field that can change is **Order.status**, and only through **setStatus()**, which the program calls once, when an order is served.
- **OrderQueue** hides `front`, `rear` and `size`, so no other class can break the links of the queue.
- **getServedOrders()** returns a **copy** of the history, so changes made outside OrderManager cannot affect the real list.
- **OrderNode's** two fields are public so the queue can link nodes easily. This is safe in practice, because no method outside OrderQueue ever receives an OrderNode: the queue keeps `front` and `rear` private and only ever returns Order objects.

**Abstraction.** Each class shows simple methods and hides how they work.
- Main calls **manager.placeOrder()** or **manager.serveNextOrder()** without knowing about nodes, pointers or the queue.
- OrderManager calls **pendingQueue.enqueue()** and **dequeue()** without handling `front`, `rear` or `next` itself.
- Main asks for **linearSearchOrder()** or **getServedOrdersSortedByAmount()** and gets the result, without seeing the search or sorting steps.

**Inheritance.** The project does not use the `extends` keyword, so there are no parent and child classes of its own. However, every Java class automatically **inherits from `java.lang.Object`**. That is where `toString()`, `equals()` and `hashCode()` come from. MenuItem and Order use this inheritance by overriding the `toString()` method they inherit.

**Polymorphism.** MenuItem and Order **override** `toString()` (marked with **@Override**), so each class gives its own text when an object is converted to a String. At Main.java:116, `System.out.println("  " + item)` joins a String with a MenuItem. Java then calls the MenuItem's own `toString()` while the program runs, printing lines like "[1] Chicken Rice Meal - ₱85.00". This is **runtime polymorphism**: the object's actual class decides which `toString()` runs. The project has no method overloading (no two methods share a name).

**Composition and Aggregation.** Classes are built from other objects ("has-a" relationships) instead of inheritance:
- **Main** has one OrderManager.
- **OrderManager** has an OrderQueue, a menu of MenuItems, and a history of Orders.
- **OrderQueue** has OrderNodes, and each **OrderNode** has one Order.
- Objects that a class creates and owns (the queue, the menu items, the nodes) are **composition**. The served orders, which come from the queue, are **aggregation**.

**Static members.** Everything in **Main** is static, because the program runs from `main()` without creating a Main object. **MIN_QUANTITY**, **MAX_QUANTITY** and **isValidCustomerName()** are static because they belong to the class, not to a particular OrderManager. This is why Main can call `OrderManager.isValidCustomerName(...)` and `OrderManager.MAX_QUANTITY` using the class name.

**Generics.** The code uses typed collections such as **List\<Order\>** and **List\<MenuItem\>**. The compiler then guarantees that a list of orders can only hold Order objects, so no casting is needed when reading from it.

**Separation of concerns.** Each class has **one job**: Main handles the screen, OrderManager handles the rules and algorithms, OrderQueue and OrderNode store the pending orders, and Order and MenuItem hold the data. This makes each part easier to understand, test and change without affecting the others.
