package com.risen.common.exception;

import com.risen.common.cosntant.HttpCodeEnum;
import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/5 17:23
 */
@Data
public class BusinessException extends RuntimeException {

    private String errorMsg;
    private Integer errorCode = HttpCodeEnum.FAIL.getCode();


    public BusinessException(Throwable cause) {
        super(cause);
    }


    public BusinessException(HttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.errorCode = httpCodeEnum.getCode();
        this.errorMsg = httpCodeEnum.getMsg();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorMsg = errorMsg;
    }


}
