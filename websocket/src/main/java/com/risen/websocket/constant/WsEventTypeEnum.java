package com.risen.websocket.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/3 15:11
 */
public enum WsEventTypeEnum {

    CONNECT("connect", "ws connect successful!"),
    CLOSE("close", "ws close successful! good-bye"),
    REGISTER("register", "ws register successful! start monitor this channel"),
    REQUEST_ERROR("error", "missing message header 'channel' field");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String msg;


    WsEventTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
