package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    //根据课程id获取课程确认信息
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id查询当前课程信息以及其讲师信息
    CourseWebVo getCourseInfoById(String courseId);

}
