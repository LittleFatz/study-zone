package com.littlefatz.algorithm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//https://leetcode.com/problems/swap-nodes-in-pairs
public class SwapNodesInPairs {
    public ListNode swapPairs(ListNode head) {

        ListNode current = head;
        if (current != null && current.next != null) {
            ListNode next = current.next;
            ListNode temp = swapPairs(next.next);
            next.next = current;
            current.next = temp;
            return next;
        } else {
            return current;
        }

    }

    public ListNode swapPairs2(ListNode head) {

        ListNode previous = new ListNode(0);
        previous.next = head;
        ListNode dummy = previous;
        ListNode current = head;
        while (current != null && current.next != null) {
            ListNode next = current.next;

            //swap 2 nodes
            current.next = next.next;
            next.next = current;

            previous.next = next;
            previous = current;

            current = current.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        List<ListNode> list = IntStream.rangeClosed(1,5).mapToObj(i -> {return new ListNode(i);}).collect(Collectors.toList());
        for (int i=0;i<list.size();i++) {
            if (i+1 < list.size()) {
                list.get(i).next = list.get(i+1);
            }
        }
        ListNode head = list.get(0);

        SwapNodesInPairs test = new SwapNodesInPairs();
        head = test.swapPairs2(head);

        while(head != null) {
            System.out.println(head.val);
            head = head.next;
        }

    }

}

