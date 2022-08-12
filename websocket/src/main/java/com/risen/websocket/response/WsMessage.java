package com.risen.websocket.response;

import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 14:47
 */
@Data
public class WsMessage {

    private WsHeader header;
    private Object body;

    private WsMessage(WsHeader header, Object body) {
        this.header = header;
        this.body = body;
    }

    public static WsMessage build(WsHeader header, Object body) {
        return new WsMessage(header, body);
    }


}
