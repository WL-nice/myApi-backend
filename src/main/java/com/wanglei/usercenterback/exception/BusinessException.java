package com.wanglei.usercenterback.exception;

import com.wanglei.usercenterback.commmon.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{

    private final int code;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMesssge());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMesssge());
        this.code = errorCode.getCode();
        this.description = description;
    }


}
