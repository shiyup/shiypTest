package com.syp.test.concurrent;

/**
 * created by shiyuping on 2021/2/5
 * <p>
 * 场景2，ThreadLocal 用作每个线程内需要独立保存信息，
 * 以便供其他方法更方便地获取该信息的场景。
 * 每个线程获取到的信息可能都是不一样的，前面执行的方法保存了信息后，
 * 后续方法可以通过 ThreadLocal 直接获取到，避免了传参，类似于全局变量的概念。
 */
public class ThreadLocalDemo2 {

    public static void main(String[] args) {
        new Service1().service1();

    }
}

/**
 * 类似于缓存，但是线程安全并且没有同步消耗
 */
class UserContextHolder {
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class Service1 {

    public void service1() {
        User user = new User("shiyp");
        UserContextHolder.holder.set(user);
        new Service2().service2();
    }
}

class Service2 {

    public void service2() {
        User user = UserContextHolder.holder.get();
        System.out.println("Service2拿到用户名：" + user.name);
        new Service3().service3();
    }
}

class Service3 {

    public void service3() {
        User user = UserContextHolder.holder.get();
        System.out.println("Service3拿到用户名：" + user.name);
        UserContextHolder.holder.remove();
    }
}

class User {

    String name;

    public User(String name) {
        this.name = name;
    }
}