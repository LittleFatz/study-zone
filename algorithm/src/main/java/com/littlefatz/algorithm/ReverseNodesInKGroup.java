package com.littlefatz.algorithm;


//https://leetcode-cn.com/problems/reverse-nodes-in-k-group/

public class ReverseNodesInKGroup {

    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode previous = dummy;
        ListNode start = head;
        ListNode end = head;

        while (start != null) {
            for (int i = 0; i < k - 1 && end != null; i++) {
                end = end.next;
            }
            if (end == null) {
                break;
            }

            ListNode next = end.next;
            end.next = null;
            ListNode subHead = reverse(start);
            previous.next = subHead;
            start.next = next;
            previous = start;
            start = next;
            end = start;

        }

        return dummy.next;

    }

    private ListNode reverse(ListNode start) {
        ListNode previous = null;
        ListNode current = start;
        while (current != null) {
            ListNode next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }

        return previous;
    }



}
