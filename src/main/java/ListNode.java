package main.java;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
public class ListNode {
    Integer val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
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

    ListNode(String values) {
        values = values.replaceAll("[\\[\\]]", "");
        if (!values.isEmpty()) {
            String[] parts = values.split(",");
            this.val = Integer.parseInt(parts[0].trim());
            ListNode last = this;
            for (int i = 1; i < parts.length; i++) {
                last.next = new ListNode(Integer.parseInt(parts[i].trim()));
                last = last.next;
            }
        }
    }

    public String toString() {
        Set<ListNode> visitedNodes = new HashSet<>();
        StringBuilder sb = new StringBuilder("[");
        ListNode curr = this;
        while (curr != null) {
            sb.append(curr.val).append(",");
            if (visitedNodes.contains(curr)) {
                sb.append("...").append(",");
                break;
            }
            visitedNodes.add(curr);
            curr = curr.next;
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }
}
