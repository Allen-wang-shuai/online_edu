package com.atguigu.eduservice.client;

import com.atguigu.eduservice.client.impl.OrderClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * service-order服务使用接口
 * 指定熔断器实现类，方法执行超时时或者执行出错时熔断器实现类中的方法
 */
@FeignClient(name = "service-order",fallback = OrderClientImpl.class)
@Component
public interface OrderClient {

    //根据用户id和课程id查询订单支付信息
    @GetMapping("/orderservice/order/isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable("memberId") String memberId, @PathVariable("courseId") String courseId);
}
