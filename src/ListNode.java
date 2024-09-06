@SuppressWarnings("unused")
public class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    ListNode(int[] values) {
        if (values.length > 0) {
            this.val = values[0];
            ListNode last = this;
            for (int i = 1; i < values.length; i++) {
                last.next = new ListNode(values[i]);
                last = last.next;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        ListNode curr = this;
        while (curr != null) {
            sb.append(curr.val).append(",");
            curr = curr.next;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }
}