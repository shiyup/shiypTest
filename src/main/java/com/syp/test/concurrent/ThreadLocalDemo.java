package com.syp.test.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by shiyuping on 2021/2/5
 * 场景1，ThreadLocal 用作保存每个线程独享的对象，
 * 为每个线程都创建一个副本，这样每个线程都可以修改自己所拥有的副本,
 * 而不会影响其他线程的副本，确保了线程安全。
 */
public class ThreadLocalDemo {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(16);

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                String date = new ThreadLocalDemo().date(finalI);
                System.out.println(date);
            });
        }
        threadPool.shutdown();
    }

    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        //创建的SimpleDateFormat对象数量=线程数
        SimpleDateFormat dateFormat = ThreadSafeFormatter.dateFormatThreadLocal.get();
        return dateFormat.format(date);
    }

    /**
     * 静态内部类单例获取
     */
    private static class ThreadSafeFormatter {
        private static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("mm:ss"));
    }

}
