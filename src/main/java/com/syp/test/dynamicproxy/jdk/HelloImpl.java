package com.syp.test.dynamicproxy.jdk;

/**
 * Created by shiyuping on 2021/3/28 11:11 下午
 */
public class HelloImpl implements HelloInterface {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
