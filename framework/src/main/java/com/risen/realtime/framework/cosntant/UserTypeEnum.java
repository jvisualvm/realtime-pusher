package com.risen.realtime.framework.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/7 18:31
 */
public enum UserTypeEnum {

    ADMIN(0, "超级管理员"),
    CUSTOM(1, "普通用户"),
    TOURIST(2, "游客");


    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String msg;


    UserTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
