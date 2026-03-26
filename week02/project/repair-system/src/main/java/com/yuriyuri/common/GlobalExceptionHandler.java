package com.yuriyuri.common;

//ai教我写的，这样就不用每次都处理异常了

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalAccessException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e){
        return Result.fail(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        //之后我再换成打日志
        e.printStackTrace();
        return Result.fail(500,"系统错误："+e.getMessage());
    }
}
