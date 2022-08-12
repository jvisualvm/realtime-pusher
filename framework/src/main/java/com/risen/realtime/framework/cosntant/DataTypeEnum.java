package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/30 23:23
 */
public enum DataTypeEnum {

    STRING(0, "字符串"),
    NUMBER(1, "数字");

    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String msg;


    DataTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
