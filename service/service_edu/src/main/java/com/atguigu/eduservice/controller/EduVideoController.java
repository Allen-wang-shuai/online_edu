package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-05-19
 */
@Api(description = "课程小节管理")
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //添加课程小节
    @ApiOperation(value = "添加课程小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除课程小节，需要同时讲阿里云中的视频删除
    @ApiOperation(value = "删除课程小节")
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        boolean flag = videoService.removeVideoById(videoId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据小节id查询小节信息
    @ApiOperation(value = "根据小节id查询小节信息")
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId){
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video",video);
    }

    //修改小节信息
    @ApiOperation(value = "修改小节信息")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

}

