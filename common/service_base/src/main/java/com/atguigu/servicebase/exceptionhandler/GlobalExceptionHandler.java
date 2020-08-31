package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类，当抛出异常时执行该类中的方法
 * 优先级为，自定义异常 > 特定异常 > 全局异常
 */
@ControllerAdvice
@Slf4j//将错误日志输出到文件
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)//设置抛出什么异常执行该方法
    @ResponseBody//为了给前台返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //特定异常处理一
    @ExceptionHandler(ArithmeticException.class)//设置抛出什么异常执行该方法
    @ResponseBody//为了给前台返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理");
    }

    //自定义异常处理
    @ExceptionHandler(GuliException.class)//设置抛出什么异常执行该方法
    @ResponseBody//为了给前台返回数据
    public R error(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }


}
