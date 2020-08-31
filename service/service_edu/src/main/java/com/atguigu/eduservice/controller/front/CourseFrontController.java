package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "课程前台管理")
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private OrderClient orderClient;

    //分页条件查询课程信息
    @ApiOperation(value = "分页条件查询课程信息")
    @PostMapping("{page}/{limit}")
    public R getFrontCourseInfo(@ApiParam(name = "page", value = "当前页码", required = true)
                                @PathVariable Long page,
                                @ApiParam(name = "limit", value = "每页记录数", required = true)
                                @PathVariable Long limit,
                                @ApiParam(name = "courseFrontVo", value = "查询对象", required = false)
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.getFrontCourseInfo(pageParam, courseFrontVo);

        return R.ok().data(map);
    }

    //根据课程ID查询课程详情
    @ApiOperation(value = "根据课程ID查询课程详情")
    @GetMapping("{courseId}")
    public R getCourseInfoById(@ApiParam(name = "courseId", value = "课程ID", required = true) @PathVariable String courseId,
                               HttpServletRequest request) {

//        System.out.println("请求请求请求发的发射点法大师傅");
        //根据课程id查询当前课程信息以及其讲师信息
        CourseWebVo courseWebVo = courseService.getCourseInfoById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

//        String token = request.getHeader("token");
//        String method = request.getMethod();
//        System.out.println("请求方法："+method);
//        System.out.println("token是：" + token);
//        System.out.println("用户id" + JwtUtils.getMemberIdByJwtToken(request));

        boolean buyCourse = false;
        //远程调用，判断课程是否已购买，因为前台会发送两次请求，第一次是测试请求，为了避免报错我们在后台先判断一下
        if (request.getHeader("token") != null && !JwtUtils.getMemberIdByJwtToken(request).equals("")) {
            buyCourse = orderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request), courseId);
        }

        return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList).data("isBuy", buyCourse);
    }

    //根据课程id查询课程信息
    @GetMapping("getCourseInfoForOder/{courseId}")
    public CourseWebVoOrder getCourseInfoForOeder(@PathVariable String courseId) {
        CourseWebVo courseInfoById = courseService.getCourseInfoById(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfoById, courseWebVoOrder);
        return courseWebVoOrder;
    }

}
