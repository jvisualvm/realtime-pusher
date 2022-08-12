package com.risen.websocket.notify;

import com.alibaba.fastjson.JSONObject;
import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.util.IfUtil;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.websocket.constant.WsEventTypeEnum;
import com.risen.websocket.handler.WebSocketHandler;
import com.risen.websocket.response.WsHeader;
import com.risen.websocket.response.WsMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 18:48
 */
public class WsMessageNotifier {

    //注册列表
    private static volatile MultiValueMap<String, WebSocketSession> sessionBindChannelMap = new LinkedMultiValueMap<>();


    public static List<WebSocketSession> get(String channel) {
        return sessionBindChannelMap.get(channel);
    }

    //注册频道
    public static void registerChannel(String channel, WebSocketSession session) {
        synchronized (sessionBindChannelMap) {
            //消息头的数据类型dataType和sessionId进行绑定
            ConditionConsumer oldConditionConsumer = () -> {
                List<WebSocketSession> lst = WsMessageNotifier.get(channel);
                if (!lst.contains(session)) {
                    lst.add(session);
                    sessionBindChannelMap.put(channel, lst);
                    sendRegisterMessage(session.getId());
                }
            };

            ConditionConsumer newConditionConsumer = () -> {
                List<WebSocketSession> lst = new ArrayList<>();
                lst.add(session);
                sessionBindChannelMap.put(channel, lst);
                sendRegisterMessage(session.getId());
            };
            IfUtil.goIf(oldConditionConsumer, newConditionConsumer, sessionBindChannelMap.containsKey(channel));
        }
    }

    //移除频道
    public static void removeChannel(String sessionId) {
        MultiValueMap<String, WebSocketSession> tempMap = new LinkedMultiValueMap<>(sessionBindChannelMap);
        tempMap.forEach((k, v) -> {
            if (v.stream().filter(s -> sessionId.equals(s.getId())).count() > 0) {
                sessionBindChannelMap.remove(k);
            }
        });
    }


    /**
     * @param message
     * @return void
     * @author: zhangxin
     * @description: 对同一个频道的收听者进行广播
     * @date: 2022/6/3 16:47
     */
    public static void sendByChannel(WsMessage message) {
        if (PredicateUtil.isAnyEmpty(message, message.getHeader(), message.getHeader().getChannel())) {
            return;
        }
        List<WebSocketSession> sessionIdList = WsMessageNotifier.getBindIdList(message.getHeader().getChannel());
        if (CollectionUtils.isEmpty(sessionIdList)) {
            return;
        }
        sessionIdList.forEach(item -> {
            String msg = JSONObject.toJSONString(message);
            try {
                item.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.debug("s send IOException error cause:{}", e.getMessage());
            }
        });
    }


    public static List<WebSocketSession> getBindIdList(String channel) {
        synchronized (sessionBindChannelMap) {
            return sessionBindChannelMap.get(channel);
        }
    }


    /**
     * @param sessionId
     * @param message
     * @return void
     * @author: zhangxin
     * @description: 单个消息
     * @date: 2022/6/3 16:47
     */
    public static void sendMessageBySessionId(String sessionId, WsMessage message) {
        WebSocketSession session = WebSocketHandler.sessionMap.get(sessionId);
        if (!WebSocketHandler.sessionMap.containsKey(sessionId) || ObjectUtils.isEmpty(session)) {
            return;
        }
        try {
            String msg = JSONObject.toJSONString(message);
            session.sendMessage(new TextMessage(msg));
            LogUtil.debug("sendMessage successful body：{}", msg);
        } catch (Exception e) {
            LogUtil.error("ws send error cause:{}", e.getMessage());
        }

    }

    public static void sendParamErrorMessage(String sessionId) {
        sendMessageBySessionId(sessionId, WsMessage.build(WsHeader.build(WsEventTypeEnum.REQUEST_ERROR.getCode()), WsEventTypeEnum.REQUEST_ERROR.getMsg()));
    }

    public static void sendConnectMessage(String sessionId) {
        sendMessageBySessionId(sessionId, WsMessage.build(WsHeader.build(WsEventTypeEnum.CONNECT.getCode()), WsEventTypeEnum.CONNECT.getMsg()));
    }

    public static void sendRegisterMessage(String sessionId) {
        sendMessageBySessionId(sessionId, WsMessage.build(WsHeader.build(WsEventTypeEnum.REGISTER.getCode()), WsEventTypeEnum.REGISTER.getMsg()));
    }


}
