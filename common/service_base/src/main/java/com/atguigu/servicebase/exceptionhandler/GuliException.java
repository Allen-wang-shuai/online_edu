package com.atguigu.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常系统不会自动抛出，需要我们手动抛出异常
@Data
@AllArgsConstructor//生成有参数的构造方法
@NoArgsConstructor//生成无参数的构造方法
public class GuliException extends RuntimeException{

    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;//异常信息

}
