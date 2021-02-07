package com.syp.test.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * created by shiyuping on 2021/2/7
 * CyclicBarrier 可以构造出一个集结点，当某一个线程执行 await() 的时候，
 * 它就会到这个集结点开始等待，等待这个栅栏被撤销。
 * 直到预定数量的线程都到了这个集结点之后，这个栅栏就会被撤销，
 * 之前等待的线程就在此刻统一出发，继续去执行剩下的任务。
 */
public class CyclicBarrierDemo {

    /**
     * 模拟：要凑齐三个人才能骑一辆车
     *
     * @param args
     */
    public static void main(String[] args) {
        //CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        //第二个参数为在“开闸”的时候执行的方法
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> System.out.println("凑齐3人了，出发！"));
        for (int i = 0; i < 6; i++) {
            new Thread(new Task(i + 1, cyclicBarrier)).start();
        }
    }

    static class Task implements Runnable {

        private int id;
        private CyclicBarrier cyclicBarrier;

        public Task(int id, CyclicBarrier cyclicBarrier) {
            this.id = id;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("同学" + id + "现在从大门出发，前往自行车驿站");
            try {
                //模拟每个同学走过来的时间
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println("同学" + id + "到了自行车驿站，开始等待其他人到达");
                cyclicBarrier.await();
                System.out.println("同学" + id + "开始骑车");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
