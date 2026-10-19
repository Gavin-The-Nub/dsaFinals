/**
 * OrderNode is a pure Java node used to construct the custom linked Queue.
 * Holds an Order reference and a pointer to the next node.
 */
public class OrderNode {
    public Order data;
    public OrderNode next;

    public OrderNode(Order data) {
        this.data = data;
        this.next = null;
    }
}
