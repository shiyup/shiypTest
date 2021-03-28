package com.syp.test.dynamicproxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by shiyuping on 2021/3/28 11:54 下午
 */
public class HelloMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before ...invoke..."+method.getName());
        Object res = methodProxy.invokeSuper(o, objects);
        System.out.println("after ...invoke..."+method.getName());
        return res;
    }
}
