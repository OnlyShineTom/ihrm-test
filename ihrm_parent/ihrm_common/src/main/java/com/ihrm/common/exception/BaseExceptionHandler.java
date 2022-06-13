package com.ihrm.common.exception;

/*
*   全局异常处理
* */

import com.ihrm.common.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
*   自定义的公共异常处理器
*       1.声明异常处理器
*       2.对异常进行统一处理
* */


@ControllerAdvice
public class BaseExceptionHandler {
    //将内容封装为json数据类型返回给前端
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result error(HttpServletRequest request, HttpServletResponse response,Exception e) throws IOException {
        e.printStackTrace();
        if(e.getClass()== CommonException.class){
            CommonException ce = (CommonException) e;
            return new Result(ce.getCode());
        }else {
            return Result.ERROR();
        }
    }

}
