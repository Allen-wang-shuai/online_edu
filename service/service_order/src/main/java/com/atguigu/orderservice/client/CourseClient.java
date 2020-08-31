package com.atguigu.orderservice.client;

import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.orderservice.client.impl.CourseClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * service-edu服务使用接口
 * 指定熔断器实现类，方法执行超时时或者执行出错时熔断器实现类中的方法
 */
@Component
@FeignClient(name = "service-edu",fallback = CourseClientImpl.class)
public interface CourseClient {

    //根据课程id查询课程信息
    @GetMapping("/eduservice/coursefront/getCourseInfoForOder/{courseId}")
    public CourseWebVoOrder getCourseInfoForOeder(@PathVariable("courseId") String courseId);
}
