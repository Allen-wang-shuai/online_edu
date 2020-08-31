package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-05-09
 */
//@CrossOrigin//解决跨域问题
@RestController
@RequestMapping("/eduservice/teacher")
@Api(description = "讲师管理")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    //查询讲师表中的所有数据
    @ApiOperation(value = "查询所有讲师")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("teachers",list);
    }

    //逻辑删除讲师
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable("id") String id){
        boolean flag = teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@ApiParam(name = "current",value = "查询页码",required = true) @PathVariable long current,
                             @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable long limit){

        try {
            int i = 1/0;
        } catch (Exception e) {//捕获时捕获所有异常，不要写自定义的，系统不认识
            //抛出时抛出我们自定义的
            throw new GuliException(20001,"执行了自定义异常处理");
        }

        //创建Page对象
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        //调用方法的时候，底层实现了封装，把分页所有数据放到了teacherPage对象中
        teacherService.page(teacherPage, null);

        long total = teacherPage.getTotal();//总记录数，注意被逻辑删除的不会被计算到总数内
        List<EduTeacher> records = teacherPage.getRecords();//数据

        //创建一个map集合，放置分页数据
//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("rows",records);
    }

    //条件加分页查询讲师
    @ApiOperation(value = "条件加分页查询讲师")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current",value = "查询页码",required = true) @PathVariable long current,
                                  @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){

        Page<EduTeacher> page = new Page<>(current,limit);
        //调用方法进行查询
        teacherService.pageQuery(page,teacherQuery);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();

        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据讲师ID查询讲师
    @ApiOperation(value = "根据讲师ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    //修改讲师
    @ApiOperation(value = "根据讲师ID修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = teacherService.updateById(eduTeacher);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

}







