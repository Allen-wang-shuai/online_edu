package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //分页条件查询课程列表
    @ApiOperation(value = "分页条件查询课程列表")
    @PostMapping("{page}/{limit}")
    public R pageQuery(@ApiParam(name = "page",value = "当前页码",required = true) @PathVariable Long page,
                       @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                       @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> coursePage = new Page<>(page,limit);

        courseService.pageQuery(coursePage,courseQuery);
        List<EduCourse> records = coursePage.getRecords();
        long total = coursePage.getTotal();

        return R.ok().data("total",total).data("rows",records);
    }

    //根据课程id删除课程所有信息
    @ApiOperation(value = "根据课程id删除课程")
    @DeleteMapping("{courseId}")
    public R removeById(@ApiParam(name = "id",value = "课程id",required = true) @PathVariable String courseId){
        boolean result = courseService.removeCourseById(courseId);
        if (result){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //添加课程基本信息
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后的课程id，为了后面添加大纲使用
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    //根据课程id查询课程基本信息
    @ApiOperation(value = "根据课程id查询课程基本信息")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfo);
    }

    //修改课程信息
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id获取课程确认信息
    @ApiOperation(value = "根据课程id获取课程确认信息")
    @GetMapping("getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getPublishCourseInfo(courseId);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    //课程最终发布
    @ApiOperation(value = "课程最终发布")
    @PutMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

}

