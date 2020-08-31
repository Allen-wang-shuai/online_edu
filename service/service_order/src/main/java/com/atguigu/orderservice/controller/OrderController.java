package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-05
 */
@Api(description = "订单管理")
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //根据课程id和用户id创建订单，返回订单编号
    @ApiOperation(value = "根据课程id和用户id创建订单，返回订单编号")
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request) {
        //根据课程id和用户id创建订单，返回订单编号
//        System.out.println("用户id："+JwtUtils.getMemberIdByJwtToken(request));
        String orderNo = orderService.createOrder(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo", orderNo);
    }

    //根据订单编号返回订单信息
    @ApiOperation(value = "根据订单编号返回订单信息")
    @GetMapping("getOorder/{orderNo}")
    public R getOrder(@PathVariable String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(queryWrapper);
        return R.ok().data("item", order);
    }

    //根据用户id和课程id查询订单支付信息
    @ApiOperation(value = "根据用户id和课程id查询订单信息")
    @GetMapping("isBuyCourse/{memberId}/{courseId}")
    public boolean isBuyCourse(@PathVariable String memberId, @PathVariable String courseId) {
        //订单状态1表示支付成功
        int count = orderService.count(new QueryWrapper<Order>().eq("member_id", memberId).eq("course_id", courseId).eq("status", 1));
        return count > 0;
    }

}

