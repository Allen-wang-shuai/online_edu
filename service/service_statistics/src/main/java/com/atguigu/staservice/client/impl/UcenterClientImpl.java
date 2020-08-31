package com.atguigu.staservice.client.impl;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.staservice.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public Integer countRegister(String day) {
        throw new GuliException(20001,"查询注册人数失败");
    }
}
