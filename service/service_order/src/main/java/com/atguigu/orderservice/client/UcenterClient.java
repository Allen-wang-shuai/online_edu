package com.atguigu.orderservice.client;

import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.orderservice.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * service-ucenter服务使用接口
 * 指定熔断器实现类，方法执行超时时或者执行出错时熔断器实现类中的方法
 */
@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {
    //根据用户id获取用户信息
    @GetMapping("/api/ucenter/getUserInfoById/{id}")
    public UserInfoOrder getUserInfoById(@PathVariable("id") String id);
}
