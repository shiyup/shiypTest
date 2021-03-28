package com.syp.test.dynamicproxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * Created by shiyuping on 2021/3/29 12:02 上午
 * 在CGLIB底层，其实是借助了ASM这个非常强大的Java字节码生成框架
 */
public class testCglib {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloImpl.class);
        enhancer.setCallback(new HelloMethodInterceptor());
        HelloImpl helloProxy = (HelloImpl)enhancer.create();
        helloProxy.sayHello();
    }
}
