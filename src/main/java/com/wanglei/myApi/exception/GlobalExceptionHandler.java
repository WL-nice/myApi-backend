package com.wanglei.myApi.exception;

import com.wanglei.myApi.commmon.BaseResponse;
import com.wanglei.myApi.commmon.ErrorCode;
import com.wanglei.myApi.commmon.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)//只捕获BusinessException异常
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);//集中记录日志
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExecptionHandler(RuntimeException e){
        log.error("runtimeException",e);//集中记录日志
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage());
    }
}
