package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //分页条件查询
    void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery);

    List<EduTeacher> get4Teacher(QueryWrapper<EduTeacher> wrapperTeacher);

    Map<String, Object> getTeacherPageList(Page<EduTeacher> pageParam);
}
