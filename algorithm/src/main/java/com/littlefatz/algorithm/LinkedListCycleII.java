package com.littlefatz.algorithm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


//https://leetcode-cn.com/problems/linked-list-cycle-ii
//快慢指针

public class LinkedListCycleII {

    public ListNode detectCycle(ListNode head) {
        Set<ListNode> index = new HashSet<>();
        ListNode current = head;
        while (current != null && current.next != null) {
            if (index.contains(current)) {
                return current;
            } else {
                index.add(current);
                current = current.next;
            }
        }

        return null;

    }

    public ListNode detectCycle2(ListNode head) {
        ListNode runner = head;
        ListNode walker = head;
        boolean meet = false;

        while (runner != null && runner.next != null) {
            runner = runner.next.next;
            walker = walker.next;
            if (runner == walker) {
                meet = true;
                break;
            }
        }
        if (meet) {
            runner = head;
            while (runner != walker) {
                runner = runner.next;
                walker = walker.next;
            }

            return walker;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        List<ListNode> list = IntStream.rangeClosed(1,5).mapToObj(i -> {return new ListNode(i);}).collect(Collectors.toList());
        for (int i=0;i<list.size();i++) {
            if (i+1 < list.size()) {
                list.get(i).next = list.get(i+1);
            }
        }
        ListNode head = list.get(0);
        list.get(4).next = list.get(1);

        LinkedListCycleII test = new LinkedListCycleII();
        head = test.detectCycle(head);
        System.out.println(head.val);


    }
}
