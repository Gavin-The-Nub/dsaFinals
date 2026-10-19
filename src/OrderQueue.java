import java.util.ArrayList;
import java.util.List;

/**
 * OrderQueue is a custom, handcrafted pure Java FIFO (First-In, First-Out) Queue.
 * Implements core data structure operations:
 * - enqueue(Order order): Appends order to rear (O(1))
 * - dequeue(): Removes and returns order at front (O(1))
 * - peek(): Examines front order without removal (O(1))
 * - isEmpty(), getSize(), getAllOrders(): Utility and traversal methods
 */
public class OrderQueue {
    private OrderNode front;
    private OrderNode rear;
    private int size;

    public OrderQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Enqueue: Adds an order to the end of the queue.
     * Time Complexity: O(1)
     */
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

    /**
     * Dequeue: Removes and returns the order at the front of the queue.
     * Time Complexity: O(1)
     */
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

    /**
     * Peek: Returns the front order without removing it.
     * Time Complexity: O(1)
     */
    public Order peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    /**
     * Checks if the queue has no elements.
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Returns the current number of orders in the queue.
     */
    public int getSize() {
        return size;
    }

    /**
     * Traverses the linked list and returns all orders in FIFO order.
     * Does not mutate or dequeue the queue.
     * Time Complexity: O(n)
     */
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
