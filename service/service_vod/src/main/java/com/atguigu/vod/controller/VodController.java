package com.atguigu.vod.controller;

import com.atguigu.commonutils.R;
import com.atguigu.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description = "视频点播管理")
@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    //视频上传
    @ApiOperation(value = "视频上传")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file) {
        //上传成功得到视频id
        String videoId = vodService.uploadVideo(file);
        return R.ok().data("videoId", videoId);
    }

    //根据视频id删除视频
    @ApiOperation(value = "根据视频id删除视频")
    @DeleteMapping("{videoId}")
    public R removeVideoById(@ApiParam(name = "videoId", value = "视频id", required = true) @PathVariable String videoId) {
        vodService.removeVideoById(videoId);
        return R.ok().message("视频删除成功");
    }

    //根据视频id批量删除视频
    @ApiOperation(value = "根据视频id批量删除视频")
    @DeleteMapping("deleteBatch")
    public R removeVideoList(@ApiParam(name = "videoIdList", value = "视频id集合", required = true)
                             @RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeVideoList(videoIdList);
        return R.ok();
    }

    //根据视频id获取视频播放凭证
    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("{videoId}")
    public R getVideoPlayAuth(@PathVariable String videoId) {
        String playAuth = vodService.getVideoPlayAuth(videoId);
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }

}
