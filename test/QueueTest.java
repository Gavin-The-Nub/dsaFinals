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
