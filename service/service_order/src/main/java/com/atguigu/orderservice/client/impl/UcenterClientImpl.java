package com.atguigu.orderservice.client.impl;

import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {

    @Override
    public UserInfoOrder getUserInfoById(String id) {
        throw new GuliException(20001,"获取用户信息失败");
    }
}
