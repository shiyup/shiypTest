package com.syp.test.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发流程控制
 * created by shiyuping on 2021/2/7
 * 用法一：一个线程等待其他多个线程都执行完毕，再继续自己的工作
 * <p>
 * 与CountDownLatch作用类似
 * 注意：CountDownLatch 是不能够重用的，
 * 比如已经完成了倒数，那可不可以在下一次继续去重新倒数呢？
 * 这是做不到的，如果有这个需求的话，可以考虑使用 CyclicBarrier 或者创建一个新的 CountDownLatch 实例
 */
public class CountDownLatchDemo1 {

    /**
     * 模拟：裁判员等待 5 个运动员都跑到终点，宣布比赛结束
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = () -> {
                try {
                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println(no + "号运动员完成了比赛。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            };
            service.submit(runnable);
        }
        System.out.println("等待5个运动员都跑完.....");
        latch.await();
        System.out.println("所有人都跑完了，比赛结束。");
    }
}

/**
 * 用法二：多个线程等待某一个线程的信号，同时开始执行
 * 与Semaphore作用类似
 */
class CountDownLatchDemo2 {

    /**
     * 模拟：运动员等裁判员，在运动员起跑之前都会等待裁判员发号施令，一声令下运动员统一起跑
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("运动员有5秒的准备时间");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = () -> {
                System.out.println(no + "号运动员准备完毕，等待裁判员的发令枪");
                try {
                    countDownLatch.await();
                    System.out.println(no + "号运动员开始跑步了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            service.submit(runnable);
        }
        Thread.sleep(5000);
        System.out.println("5秒准备时间已过，发令枪响，比赛开始！");
        countDownLatch.countDown();
    }
}
