package com.risen.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.util.IfUtil;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.websocket.notify.WsMessageNotifier;
import com.risen.websocket.request.WsRequestMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 13:52
 */
public class WebSocketHandler extends AbstractWebSocketHandler {

    public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
        //通知连接成功
        WsMessageNotifier.sendConnectMessage(session.getId());
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ConditionConsumer consumer = () -> {
            messageValid(session, message);
        };
        IfUtil.goIf(consumer, sessionMap.containsKey(session.getId()));
    }


    private void messageValid(WebSocketSession session, WebSocketMessage<?> message) {
        //对消息进行简单的校验
        WsRequestMessage wsMessage = null;
        try {
            wsMessage = JSONObject.parseObject(message.getPayload().toString(), WsRequestMessage.class);
        } catch (Exception e) {
            WsMessageNotifier.sendParamErrorMessage(session.getId());
            sessionMap.remove(session.getId());
            LogUtil.error("handleMessage json parse error:{}", e.getMessage());
            close(session);
        }
        if (PredicateUtil.isAnyEmpty(wsMessage)) {
            WsMessageNotifier.sendParamErrorMessage(session.getId());
            sessionMap.remove(session.getId());
            close(session);
            return;
        }
        //注册频道
        WsMessageNotifier.registerChannel(wsMessage.getChannel(), session);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessionMap.remove(session.getId());
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());
        WsMessageNotifier.removeChannel(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    private void close(WebSocketSession webSocketSession) {
        try {
            //先移除监听再关闭
            WsMessageNotifier.removeChannel(webSocketSession.getId());
            webSocketSession.close();
        } catch (Exception e) {
            LogUtil.error("ws close error:{}", e.getMessage());
        }
    }

}