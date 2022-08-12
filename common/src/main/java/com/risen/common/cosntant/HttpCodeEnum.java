package com.risen.common.cosntant;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/4 13:07
 */
public enum HttpCodeEnum {

    SUCCESS(HttpCode.SUCCESS_CODE, "请求成功"),
    FAIL(HttpCode.FAIL_CODE, "请求失败"),
    FAIL_PARAM_ADD(5000001, "参数错误"),
    FAIL_PARAM_EXISTS(5000009, "记录已经存在，无法新增"),
    FAIL_LOG_CONFIG(5000100, "日志entity必须继承LogTemplate才能使用本jar自带的日志记录功能");

    HttpCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    @Setter
    private Integer code;

    @Getter
    @Setter
    private String msg;

}
