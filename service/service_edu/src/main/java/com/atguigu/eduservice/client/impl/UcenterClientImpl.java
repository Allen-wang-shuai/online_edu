package com.atguigu.eduservice.client.impl;

import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.vo.UserInfo;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

/**
 * 当UcenterClient中的方法执行超时时或者执行出错时执行这里的方法
 */
@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public UserInfoOrder getUserInfoById(String id) {
        throw new GuliException(20001,"获取用户信息失败");
    }
}
