package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    //视频上传
    String uploadVideo(MultipartFile file);

    //根据视频id删除视频
    void removeVideoById(String videoId);

    //根据视频id批量删除视频
    void removeVideoList(List<String> videoIdList);

    String getVideoPlayAuth(String videoId);
}
