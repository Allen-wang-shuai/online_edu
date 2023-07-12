package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.UserInfoOrder;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.vo.UserInfo;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
@Api(description = "课程评论")
@RestController
@RequestMapping("/eduservice/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id分页查询评论信息
    @ApiOperation(value = "课程评论列表")
    @GetMapping("{page}/{limit}")
    public R getCourseCommentInfo(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                  @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                                  @ApiParam(name = "courseId", value = "课程id", required = true) String courseId) {
        Map<String, Object> map = commentService.getCourseCommentInfo(page, limit, courseId);
        return R.ok().data(map);
    }

    //添加评论
    @ApiOperation(value = "添加评论")
    @PostMapping("addComment")
    public R addComment(@RequestBody EduComment eduComment, HttpServletRequest request){
        //通过JWT工具类获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //判断如果用户没有登录提示登录
        if (StringUtils.isEmpty(memberId)){
            return R.error().code(28004).message("请您登录后评论");
        }
        //向评论对象中完善参数
        eduComment.setMemberId(memberId);
        //获取用户信息
        UserInfoOrder userInfo = ucenterClient.getUserInfoById(memberId);
        eduComment.setNickname(userInfo.getNickname());
        eduComment.setAvatar(userInfo.getAvatar());

        //添加评论
        commentService.save(eduComment);
        return R.ok();
    }

}

