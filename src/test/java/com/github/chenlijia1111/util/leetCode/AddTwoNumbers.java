package com.github.chenlijia1111.util.leetCode;

import java.util.Objects;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 示例：
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/4 0004 下午 2:46
 **/
public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //防止为空
        l1 = Objects.isNull(l1) ? new ListNode(0) : l1;
        l2 = Objects.isNull(l2) ? new ListNode(0) : l2;
        //值
        int sumVal = l1.val + l2.val;
        //判断是否超过了9,如果超过了9需要进位---除以10 如果大于等于10,进位
        int i = sumVal / 10;
        //对10求余,得到真实值
        int i1 = sumVal % 10;
        ListNode listNode = new ListNode(i1);
        //需要进位
        if (i >= 1) {
            if (Objects.isNull(l1.next) && Objects.isNull(l2.next)) {
                ListNode nextNode = l1.next;
                nextNode = Objects.isNull(nextNode) ? new ListNode(i) : nextNode;
                listNode.next = addTwoNumbers(nextNode, l2.next);
            } else {
                ListNode nextNode = l1.next;
                nextNode = Objects.isNull(nextNode) ? new ListNode(0) : nextNode;
                nextNode.val += i;
                listNode.next = addTwoNumbers(nextNode, l2.next);
            }
        } else {
            if (Objects.nonNull(l1.next) || Objects.nonNull(l2.next)) {
                listNode.next = addTwoNumbers(l1.next, l2.next);
            }
        }

        return listNode;
    }


    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


}
