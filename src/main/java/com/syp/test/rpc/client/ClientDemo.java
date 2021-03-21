package com.syp.test.rpc.client;

import com.syp.test.rpc.RpcClient;

/**
 * Created by shiyuping on 2021/3/21 10:50 下午
 */
public class ClientDemo {
    public static void main(String[] args) throws Exception {
        RpcClient client = new RpcClient();
        CalculatorService service = client.refer(CalculatorService.class, "127.0.0.1", 1234);
        int result = service.add(2, 4);
        System.out.println("result:" + result);
    }
}
