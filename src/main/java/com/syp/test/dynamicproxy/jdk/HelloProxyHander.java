package com.syp.test.dynamicproxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by shiyuping on 2021/3/28 11:14 下午
 */
public class HelloProxyHander implements InvocationHandler {
    private Object subject;

    public HelloProxyHander(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before ...invoke..."+method.getName());
        method.invoke(subject,args);
        System.out.println("after ...invoke..."+method.getName());
        return null;
    }
}
