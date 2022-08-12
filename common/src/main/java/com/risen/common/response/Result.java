package com.risen.common.response;

import com.alibaba.fastjson.JSON;
import com.risen.common.cosntant.HttpCode;
import com.risen.common.cosntant.HttpCodeEnum;
import com.risen.common.cosntant.Symbol;
import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/4 13:05
 */
@Data
public class Result<T> {

    private Integer code;
    private Object data;
    private String msg;

    public Result() {
        this.code = HttpCodeEnum.SUCCESS.getCode();
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
    }

    public Result<T> failWithObj(Object msg) {
        this.code = HttpCodeEnum.FAIL.getCode();
        this.msg = JSON.toJSONString(msg);
        return this;
    }

    public Result<T> failWithMsg(String msg) {
        this.code = HttpCodeEnum.FAIL.getCode();
        this.msg = msg;
        return this;
    }

    public Result<T> failWithMsg(String msg, Integer code) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> failWithCode(HttpCodeEnum httpCode) {
        this.code = httpCode.getCode();
        this.msg = httpCode.getMsg();
        return this;
    }

    public Result<T> failWithCode(T response, HttpCodeEnum httpCode) {
        this.code = httpCode.getCode();
        this.msg = httpCode.getMsg();
        this.data = response;
        return this;
    }

    public Result<T> failWithCode(T response, HttpCodeEnum httpCode, String msg) {
        this.code = httpCode.getCode();
        StringBuilder builder = new StringBuilder();
        builder.append(httpCode.getMsg()).append(Symbol.SYMBOL_SPACE).append(msg);
        this.msg = builder.toString();
        this.data = response;
        return this;
    }

    public Result<T> successWithBody(T response) {
        this.code = HttpCode.SUCCESS_CODE;
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
        this.data = response;
        return this;
    }

    public Result<T> successIgnoreBody() {
        this.code = HttpCode.SUCCESS_CODE;
        this.msg = HttpCodeEnum.SUCCESS.getMsg();
        return this;
    }

    public boolean ifSuccess() {
        if (HttpCode.SUCCESS_CODE.equals(code)) {
            return true;
        }
        return false;
    }

}
