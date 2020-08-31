package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.entity.vo.UserInfo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-06-10
 */
@Api(description = "用户登录注册管理")
@RestController
@RequestMapping("/api/ucenter")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return R.ok().data("token",token);
    }

    //注册
    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取登录信息，注意这里的token是从请求头里面获得的，并不是从cookie中获得的
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UserInfo userInfo = memberService.getUserInfo(memberId);
            return R.ok().data("item",userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"token信息异常");
        }
    }

    //根据用户id获取用户信息
    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("getUserInfoById/{id}")
    public UserInfoOrder getUserInfoById(@PathVariable String id){
        UcenterMember ucenterMember = memberService.getById(id);
        UserInfoOrder userInfoOrder = new UserInfoOrder();
        BeanUtils.copyProperties(ucenterMember,userInfoOrder);
        return userInfoOrder;
    }

    //查询某一天的注册人数
    @ApiOperation(value = "查询某一天的注册人数")
    @GetMapping("countRegister/{day}")
    public Integer countRegister(@PathVariable String day){
        return memberService.countRegister(day);
    }

}

