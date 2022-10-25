package com.itheima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    //该注解是异常处理方法 一旦Controller抛出一下异常，controller则会捕获到
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){

        log.error(exception.getMessage());

        if (exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split("");
            String message = split[2]+"已存在";
            return R.error(message);

        }
        return R.error("未知错误");

    }
    //该注解是异常处理方法 一旦Controller抛出一下异常，controller则会捕获到
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException customException){

        log.error(customException.getMessage());

//        if (customException.getMessage().contains("Duplicate entry")){
//            String[] split = customException.getMessage().split("");
//            String message = split[2]+"已存在";
//            return R.error(message);
//
//        }

        return R.error(customException.getMessage());

    }
}
