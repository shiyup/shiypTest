package com.syp.test.dynamicproxy.jdk;

import java.lang.reflect.Proxy;

/**
 * Created by shiyuping on 2021/3/28 11:12 下午
 */
public class testJdk {
    public static void main(String[] args) {
        HelloImpl hello = new HelloImpl();
        HelloInterface helloProxy = (HelloInterface)Proxy.newProxyInstance(hello.getClass().getClassLoader(),
                hello.getClass().getInterfaces(), new HelloProxyHander(hello));
        helloProxy.sayHello();
    }
}
