package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.orderservice.client.CourseClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.util.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private CourseClient courseClient;

    //根据课程id和用户id创建订单，返回订单编号
    @Override
    public String createOrder(String courseId, String memberIdByJwtToken) {
        //通过远程调用根据用户id获取用户信息
        UserInfoOrder userInfoOrder = ucenterClient.getUserInfoById(memberIdByJwtToken);
        //通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseWebVoOrder = courseClient.getCourseInfoForOeder(courseId);

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseWebVoOrder.getTitle());
        order.setCourseCover(courseWebVoOrder.getCover());
        order.setTeacherName(courseWebVoOrder.getTeacherName());
        order.setTotalFee(courseWebVoOrder.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);//支付状态，0未支付
        order.setPayType(1);//支付类型，默认微信
        baseMapper.insert(order);

        //返回订单编号
        return order.getOrderNo();
    }
}
