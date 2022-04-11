package com.syp.test.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author shiyuping
 * @Date 2022/4/11 17:38
 */
@Configuration
@EnableWebSocket
public class WebSocketAutoConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // webSocket通道
        // 指定处理器和路径
        registry.addHandler(new MyWebSocketHandler(), "/websocket")
                // 指定自定义拦截器
                .addInterceptors(new MyWebSocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
        /*// sockJs通道
        registry.addHandler(new WebSocketHandler(), "/sock-js")
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins("*")
                // 开启sockJs支持
                .withSockJS();*/
    }
}
