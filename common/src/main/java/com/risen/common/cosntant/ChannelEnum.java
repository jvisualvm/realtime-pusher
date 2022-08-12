package com.risen.common.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/24 22:16
 */
public enum ChannelEnum {

    LOG("log"),
    PROCESS("process");
    @Getter
    @Setter
    private String code;

    ChannelEnum(String code) {
        this.code = code;
    }
}



