package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author wangshuai
 * @since 2022-04-05
 */
public interface OrderService extends IService<Order> {

    //根据课程id和用户id创建订单，返回订单编号
    String createOrder(String courseId, String memberIdByJwtToken);
}
