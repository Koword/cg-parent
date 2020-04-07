package com.changgou.framework.exception;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description 公共异常处理类
 * @Author tangKai
 * @Date 11:46 2019/12/26
 **/
@ControllerAdvice //@ControllerAdvice注解，全局捕获异常类，只要作用在@RequestMapping上，所有的异常都会被捕获。
public class BaseExceptionHandler {



    /**
     * @Description 异常处理
     * @Author tangKai
     * @Date 11:48 2019/12/26
     * @Param [e]
     * @Return entity.Result
     **/
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,"公共异常处理类之--"+e.getMessage());

    }

}
