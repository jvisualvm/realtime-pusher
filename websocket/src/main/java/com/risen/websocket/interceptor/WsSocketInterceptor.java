package com.risen.websocket.interceptor;

import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.util.IfUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 13:52
 */
public class WsSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        AtomicReference<Boolean> isConnect = new AtomicReference(true);
        ConditionConsumer consumer = () -> {
            //TODO 等待添加握手之前的校验逻辑 暂时不校验 后续有身份认证需求再添加逻辑

        };

        IfUtil.goIf(consumer, false);
        return isConnect.get();
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}