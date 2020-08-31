package com.atguigu.eduservice.client.impl;

import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBuyCourse(String memberId, String courseId) {
        throw new GuliException(20001,"查询订单支付信息失败");
    }
}
