package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "讲师前台管理")
@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    //1、分页查询讲师
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("{page}/{limit}")
    public R getTeacherPageList(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        Map<String, Object> teacherMap = teacherService.getTeacherPageList(pageParam);
        return R.ok().data(teacherMap);
    }

    //2、根据讲师id查询讲师信息
    @ApiOperation(value = "根据讲师id查询讲师信息")
    @GetMapping("{id}")
    public R getById(@ApiParam(name = "id",value = "讲师id") @PathVariable String id){
        //讲师信息
        EduTeacher teacher = teacherService.getById(id);
        //根据讲师id查询这个讲师的课程列表
        List<EduCourse> courseList = courseService.selectByTeacherId(id);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

}
