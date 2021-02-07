package com.syp.test.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by shiyuping on 2021/2/7
 * Condition接口
 * condition.await() 等待
 * condition.signal() 唤醒
 */
public class ConditionDemo {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo conditionDemo = new ConditionDemo();
        new Thread(() -> {
            try {
                conditionDemo.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        conditionDemo.method1();
    }

    void method1() throws InterruptedException {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":条件不满足，开始await");
            condition.await();
            System.out.println(Thread.currentThread().getName() + ":条件满足了，开始执行后续的任务");
        } finally {
            lock.unlock();
        }
    }

    void method2() throws InterruptedException {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":需要5秒钟的准备时间");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + ":准备工作完成，唤醒其他的线程");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
