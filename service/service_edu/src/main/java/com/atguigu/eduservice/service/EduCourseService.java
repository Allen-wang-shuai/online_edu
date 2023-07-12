package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getPublishCourseInfo(String courseId);

    void pageQuery(Page<EduCourse> coursePage, CourseQuery courseQuery);

    boolean removeCourseById(String courseId);

    List<EduCourse> get8Course(QueryWrapper<EduCourse> wrapperCourse);

    List<EduCourse> selectByTeacherId(String id);

    Map<String, Object> getFrontCourseInfo(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo);

    CourseWebVo getCourseInfoById(String courseId);

    //更新课程浏览数
    void updateCourseInfoViewCount(String id);
}
