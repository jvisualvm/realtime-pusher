package com.risen.websocket.config;

import com.risen.websocket.handler.WebSocketHandler;
import com.risen.websocket.interceptor.WsSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 13:52
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/ws")
                .addInterceptors(new WsSocketInterceptor())
                .setAllowedOrigins("*");
    }

}
