package com.syp.test.rpc.server;


/**
 * Created by shiyuping on 2021/3/21 10:59 下午
 */
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
