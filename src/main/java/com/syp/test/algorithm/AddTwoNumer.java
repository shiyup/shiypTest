package com.syp.test.algorithm;

import cn.hutool.json.JSONUtil;

/**
 * 给你两个非空 的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0开头。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 *
 * @Author shiyuping
 * @Date 2021/11/18 10:03
 */
public class AddTwoNumer {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2, new ListNode(4, new ListNode(3, null)));
        ListNode l2 = new ListNode(5, new ListNode(6, new ListNode(4, null)));
        AddTwoNumer addTwoNumer = new AddTwoNumer();
        ListNode listNode = addTwoNumer.addTwoNumbers(l1, l2);
        System.out.println(JSONUtil.toJsonStr(listNode));
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        /* 进位 */
        int carry = 0;
        /* 起始位 */
        ListNode res = new ListNode(0);
        ListNode real = res;
        while (l1 != null || l2 != null) {
            int x = l1 == null ? 0 : l1.val;
            int y = l2 == null ? 0 : l2.val;
            int sum = x + y + carry;
            carry = sum / 10;
            sum = sum % 10;
            real.next = new ListNode(sum);
            real = real.next;

            if(l1 != null){
                l1 = l1.next;
            }
            if(l2 != null){
                l2 = l2.next;
            }

        }
        if (carry == 1) {
            real.next = new ListNode(1);
        }
        return res;
    }

    static class ListNode {
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

    }
}
