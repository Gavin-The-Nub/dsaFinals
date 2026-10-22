import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Main is the console user interface driver for the Canteen Order Queue Management System.
 * Designed for the De La Salle Lipa CITE Datastrc Final Project.
 * Features:
 * [1] Place Order (Queue Enqueue)
 * [2] Serve Next Order (Queue Dequeue)
 * [3] Search Order by ID (Linear Search)
 * [4] View Pending Orders (Queue Traversal)
 * [5] View Completed Orders Sorted by Amount (Insertion Sort)
 * [6] View Canteen Menu
 * [7] Exit
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderManager manager = new OrderManager();

    // Receipt box layout: "| " + 14-char label + " : " + value + " |"
    private static final int RECEIPT_VALUE_WIDTH = 36;
    private static final String RECEIPT_BORDER = "+" + "-".repeat(RECEIPT_VALUE_WIDTH + 19) + "+";

    public static void main(String[] args) {
        enableUtf8Output();
        boolean running = true;
        while (running) {
            printHeader();
            System.out.println(" [1] Place Order");
            System.out.println(" [2] Serve Next Order");
            System.out.println(" [3] Search Order by ID");
            System.out.println(" [4] View Pending Orders");
            System.out.println(" [5] View Completed Orders Sorted by Amount");
            System.out.println(" [6] View Canteen Menu");
            System.out.println(" [7] Exit");
            System.out.println("=========================================================================");
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
                    if (confirm("Are you sure you want to exit?")) {
                        System.out.println("Thank you for using the Canteen Order Queue Management System!");
                        running = false;
                    }
                }
            }

            if (running) {
                System.out.println();
                System.out.print("Press ENTER to return to main menu...");
                readLine();
            }
        }
    }

    /**
     * Windows consoles default to a code page without the peso sign (₱), which then prints as "?".
     * Switch the console to UTF-8 (code page 65001) and make System.out write UTF-8.
     */
    private static void enableUtf8Output() {
        if (System.getProperty("os.name", "").startsWith("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "chcp 65001 >nul")
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                        .redirectError(ProcessBuilder.Redirect.INHERIT)
                        .start().waitFor();
            } catch (Exception e) {
                // not fatal: symbols may just display incorrectly
            }
        }
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
    }

    private static void printHeader() {
        System.out.println("\n=========================================================================");
        System.out.println("         CANTEEN ORDER QUEUE MANAGEMENT SYSTEM");
        System.out.println("=========================================================================");
        System.out.printf("  Pending Orders in Queue: %-4d |  Total Served Orders: %-4d%n",
                manager.getPendingOrders().size(), manager.getServedOrders().size());
        System.out.println("-------------------------------------------------------------------------");
    }

    /**
     * Feature 1: Place Order
     * Prompts for customer info, displays menu, calculates price, and enqueues order.
     */
    private static void handlePlaceOrder() {
        System.out.println("--- [1] PLACE NEW ORDER ---");
        System.out.print("Enter Customer Name: ");
        String customerName = readLine().trim();
        while (!OrderManager.isValidCustomerName(customerName)) {
            if (customerName.isEmpty()) {
                System.out.print("Customer name cannot be empty. Please enter name: ");
            } else {
                System.out.print("Customer name must contain letters, not just numbers. Please enter name: ");
            }
            customerName = readLine().trim();
        }

        System.out.println("\n--- CANTEEN MENU ---");
        List<MenuItem> menu = manager.getMenu();
        for (MenuItem item : menu) {
            System.out.println("  " + item);
        }

        System.out.print("\nSelect Menu Item Number (1-" + menu.size() + "): ");
        int itemChoice = readIntChoice(1, menu.size());

        System.out.print("Enter Quantity (" + OrderManager.MIN_QUANTITY + "-" + OrderManager.MAX_QUANTITY + "): ");
        int quantity = readIntChoice(OrderManager.MIN_QUANTITY, OrderManager.MAX_QUANTITY);

        Order newOrder = manager.placeOrder(customerName, itemChoice, quantity);
        if (newOrder != null) {
            System.out.println("\n>>> ORDER PLACED SUCCESSFULLY (Enqueued to Queue) <<<");
            System.out.println(RECEIPT_BORDER);
            printReceiptRow("Order ID", "#" + newOrder.getOrderId());
            printReceiptRow("Customer", newOrder.getCustomerName());
            printReceiptRow("Item", newOrder.getItemName());
            printReceiptRow("Quantity", String.valueOf(newOrder.getQuantity()));
            printReceiptRow("Total Amount", peso(newOrder.getTotalAmount()));
            printReceiptRow("Status", newOrder.getStatus());
            printReceiptRow("Queue Position", String.valueOf(manager.getPendingOrders().size()));
            printReceiptRow("Timestamp", newOrder.getTimestamp());
            System.out.println(RECEIPT_BORDER);
        }
    }

    /**
     * Feature 2: Serve Next Order
     * Dequeues the front order from the queue and marks it as served.
     */
    private static void handleServeNextOrder() {
        System.out.println("--- [2] SERVE NEXT ORDER ---");
        Order next = manager.peekNextOrder();
        if (next == null) {
            System.out.println("The queue is empty. No pending orders to serve.");
            return;
        }

        System.out.printf("Next in queue: Order #%d - %s (%s x%d, Total: ₱%.2f)%n",
                next.getOrderId(), next.getCustomerName(), next.getItemName(),
                next.getQuantity(), next.getTotalAmount());
        if (!confirm("Serve this order?")) {
            System.out.println("Serve cancelled. The queue is unchanged.");
            return;
        }

        Order served = manager.serveNextOrder();
        System.out.println(">>> ORDER SERVED SUCCESSFULLY (Dequeued from Queue) <<<");
        System.out.println(RECEIPT_BORDER);
        printReceiptRow("Order ID", "#" + served.getOrderId());
        printReceiptRow("Customer", served.getCustomerName());
        printReceiptRow("Item", served.getItemName());
        printReceiptRow("Quantity", String.valueOf(served.getQuantity()));
        printReceiptRow("Total Paid", peso(served.getTotalAmount()));
        printReceiptRow("Status", served.getStatus());
        System.out.println(RECEIPT_BORDER);
    }

    /**
     * Feature 3: Search Order by ID
     * Uses Linear Search to find an order by its ID in the pending queue or served list.
     */
    private static void handleSearchOrder() {
        System.out.println("--- [3] SEARCH ORDER BY ID (LINEAR SEARCH) ---");
        System.out.print("Enter Order ID to search (e.g. 1001): ");
        int orderId = readIntChoice(1, Integer.MAX_VALUE);

        long startTime = System.nanoTime();
        Order found = manager.linearSearchOrder(orderId);
        long endTime = System.nanoTime();
        double elapsedMicroseconds = (endTime - startTime) / 1000.0;

        if (found == null) {
            System.out.println("Order not found");
        } else {
            System.out.println(">>> ORDER FOUND VIA LINEAR SEARCH <<<");
            System.out.println(RECEIPT_BORDER);
            printReceiptRow("Order ID", "#" + found.getOrderId());
            printReceiptRow("Customer", found.getCustomerName());
            printReceiptRow("Item", found.getItemName());
            printReceiptRow("Quantity", String.valueOf(found.getQuantity()));
            printReceiptRow("Total Amount", peso(found.getTotalAmount()));
            printReceiptRow("Current Status", found.getStatus());
            printReceiptRow("Ordered At", found.getTimestamp());
            System.out.println(RECEIPT_BORDER);
            System.out.printf("Search Algorithm: Linear Search | Time elapsed: %.3f µs%n", elapsedMicroseconds);
        }
    }

    /**
     * Feature 4A: View Pending Orders
     * Traverses the queue in FIFO order.
     */
    private static void handleViewPendingOrders() {
        System.out.println("--- [4] PENDING ORDERS QUEUE (FIFO ORDER) ---");
        List<Order> pending = manager.getPendingOrders();
        if (pending.isEmpty()) {
            System.out.println("ℹ The queue is empty. No pending orders!");
            return;
        }

        printOrderTable(pending, true);
    }

    /**
     * Feature 4B: View Completed Orders Sorted by Amount
     * Applies custom Insertion Sort algorithm.
     */
    private static void handleViewCompletedOrdersSorted() {
        System.out.println("--- [5] COMPLETED ORDERS SORTED BY AMOUNT (INSERTION SORT) ---");
        List<Order> completed = manager.getServedOrders();
        if (completed.isEmpty()) {
            System.out.println("No served orders yet.");
            return;
        }

        long startTime = System.nanoTime();
        List<Order> sorted = manager.getServedOrdersSortedByAmount(true);
        long endTime = System.nanoTime();
        double elapsedMicroseconds = (endTime - startTime) / 1000.0;

        printOrderTable(sorted, false);
        System.out.printf("Sorting Algorithm: Insertion Sort | Records: %d | Time elapsed: %.3f µs%n",
                sorted.size(), elapsedMicroseconds);
    }

    /**
     * Feature 6: View Canteen Menu
     */
    private static void handleViewMenu() {
        System.out.println("--- [6] CANTEEN MENU CATALOG ---");
        System.out.println("---------------------------------------------------------------");
        System.out.printf(" %-5s | %-32s | %-12s%n", "No.", "Meal / Beverage", "Price (PHP)");
        System.out.println("---------------------------------------------------------------");
        for (MenuItem item : manager.getMenu()) {
            System.out.printf(" %-5d | %-32s | ₱%-11.2f%n", item.getId(), item.getName(), item.getPrice());
        }
        System.out.println("---------------------------------------------------------------");
    }

    private static void printOrderTable(List<Order> list, boolean isPending) {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf(" %-5s | %-9s | %-20s | %-26s | %-4s | %-11s | %-8s%n",
                isPending ? "Pos" : "No.", "Order ID", "Customer", "Item", "Qty", "Total", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------");
        int index = 1;
        for (Order o : list) {
            System.out.printf(" %-5d | #%-8d | %-20s | %-26s | %-4d | ₱%-10.2f | %-8s%n",
                    index++,
                    o.getOrderId(),
                    truncate(o.getCustomerName(), 20),
                    truncate(o.getItemName(), 26),
                    o.getQuantity(),
                    o.getTotalAmount(),
                    o.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
    }

    /** Prints one "| Label : value |" line of a receipt box; every row has the same width as RECEIPT_BORDER. */
    private static void printReceiptRow(String label, String value) {
        System.out.printf("| %-14s : %-" + RECEIPT_VALUE_WIDTH + "s |%n", label, truncate(value, RECEIPT_VALUE_WIDTH));
    }

    private static String peso(double amount) {
        return String.format("₱%.2f", amount);
    }

    private static String truncate(String text, int max) {
        if (text == null) return "";
        if (text.length() <= max) return text;
        return text.substring(0, max - 3) + "...";
    }

    /**
     * Reads one line; if input has ended (e.g. Ctrl+D / Ctrl+Z), exits cleanly instead of crashing.
     */
    private static String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("\nInput closed. Exiting program.");
            System.exit(0);
        }
        return scanner.nextLine();
    }

    /**
     * Reads a whole number in [min, max]. Only plain digits are accepted, so letters,
     * decimals, signs, and blank input are rejected with a short message and re-prompted.
     */
    private static int readIntChoice(int min, int max) {
        while (true) {
            String line = readLine().trim();
            if (line.matches("[0-9]+")) {
                try {
                    int val = Integer.parseInt(line);
                    if (val >= min && val <= max) {
                        return val;
                    }
                } catch (NumberFormatException e) {
                    // too large for an int: falls through to the error message
                }
            }
            if (max == Integer.MAX_VALUE) {
                System.out.print("Invalid input. Enter a valid number: ");
            } else {
                System.out.printf("Invalid input. Enter a number from %d to %d: ", min, max);
            }
        }
    }

    /** Asks a Y/N question and repeats until the answer is Y or N. */
    private static boolean confirm(String question) {
        while (true) {
            System.out.print(question + " (Y/N): ");
            String answer = readLine().trim();
            if (answer.equalsIgnoreCase("Y")) return true;
            if (answer.equalsIgnoreCase("N")) return false;
            System.out.println("Invalid input. Enter Y or N.");
        }
    }
}
