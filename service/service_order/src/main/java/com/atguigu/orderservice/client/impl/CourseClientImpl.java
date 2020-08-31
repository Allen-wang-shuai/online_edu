package com.atguigu.orderservice.client.impl;

import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.orderservice.client.CourseClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class CourseClientImpl implements CourseClient {

    @Override
    public CourseWebVoOrder getCourseInfoForOeder(String courseId) {
        throw new GuliException(20001,"获取课程信息失败");
    }
}
