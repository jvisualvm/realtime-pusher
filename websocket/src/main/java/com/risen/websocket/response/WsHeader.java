package com.risen.websocket.response;

import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 15:25
 */
@Data
public class WsHeader {

    private String channel;

    private WsHeader(String channel) {
        this.channel = channel;
    }

    public static WsHeader build(String channel) {
        return new WsHeader(channel);
    }

}
