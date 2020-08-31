package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin//解决跨域问题，通过网关解决了
@RestController
@RequestMapping("/eduservice/user")
@Api(description = "用户登录相关接口")
public class EduLoginController {

    //用户登录
    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public R login(){
        return R.ok().data("token","admin");
    }

    //获取用户信息
    @GetMapping("info")
    @ApiOperation(value = "获取用户信息")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

}
