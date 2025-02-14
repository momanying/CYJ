package com.lyh.springboot.exception;

import com.lyh.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.lyh.springboot.controller")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回JSON串
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody //返回JSON串
    public Result error(CustomException e) {
        return Result.error(e.getCode(),e.getMessage());
    }
}
