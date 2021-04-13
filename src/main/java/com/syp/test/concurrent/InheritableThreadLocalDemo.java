package com.syp.test.concurrent;

/**
 * created by shiyuping on 2021/4/13
 * 父子线程可以通过InheritableThreadLocal共享数据
 */
public class InheritableThreadLocalDemo {
    /**
     * 主要不同点在于重写了ThreadLocal的getMap和createMap方法获取的是Thread里的inheritableThreadLocals属性
     * 能共享数据的主要原因得看Thread的构造方法中init方法里
     * parent就是父线程
     * if (parent.inheritableThreadLocals != null)
     * this.inheritableThreadLocals = ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
     */
    private static InheritableThreadLocal<Integer> inheritableThreadLocalData = new InheritableThreadLocal<>();

    private static ThreadLocal<Integer> threadLocalData = new ThreadLocal<>();


    public static void main(String[] args) {
        inheritableThreadLocalData.set(1);
        threadLocalData.set(1);
        System.out.println("current thread get InheritableThreadLocal data:" + inheritableThreadLocalData.get() + " get ThreadLocal data:" + threadLocalData.get());
        new Thread(() -> System.out.println("son thread get InheritableThreadLocal data:" + inheritableThreadLocalData.get() + " get ThreadLocal data:" + threadLocalData.get())).start();
    }

}
