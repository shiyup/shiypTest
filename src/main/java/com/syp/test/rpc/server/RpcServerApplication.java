package com.syp.test.rpc.server;

import com.syp.test.rpc.RpcServer;

/**
 * Created by shiyuping on 2021/3/21 11:00 下午
 */
public class RpcServerApplication {
    public static void main(String[] args) throws Exception {
        CalculatorService service = new CalculatorServiceImpl();
        RpcServer server = new RpcServer();
        server.export(service, 1234);
    }
}
