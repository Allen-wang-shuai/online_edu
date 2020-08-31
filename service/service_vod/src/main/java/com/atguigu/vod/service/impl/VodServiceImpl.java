package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.AliyunVodSDKUtils;
import com.atguigu.vod.utils.ConstantPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    //视频上传
    @Override
    public String uploadVideo(MultipartFile file) {

        try {

            //fileName：视频文件原始名称
            String fileName = file.getOriginalFilename();
            //title：上传视频控制台显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //inputStream：视频文件输入流
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = response.getVideoId();
            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if (StringUtils.isEmpty(videoId)) {
                    throw new GuliException(20001, errorMessage);
                }
            }

            return videoId;

        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频上传失败");
        }
    }

    //根据视频id删除视频
    @Override
    public void removeVideoById(String videoId) {

        try {
            //通过工具类获得初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //获得删除request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //向request对象中设置视频id
            request.setVideoIds(videoId);

            DeleteVideoResponse response = client.getAcsResponse(request);

        } catch (ClientException e) {
            throw new GuliException(20001, "视频删除失败");
        }

    }

    //根据视频id批量删除视频
    @Override
    public void removeVideoList(List<String> videoIdList) {
        try {
            //通过工具类获得初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //获得删除request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //将视频id集合转化为以逗号分割的字符串
            String ids = StringUtils.join(videoIdList, ",");

            //向request对象中设置视频ids，注意一次智能批量删除20个
            request.setVideoIds(ids);

            DeleteVideoResponse response = client.getAcsResponse(request);

        } catch (ClientException e) {
            throw new GuliException(20001, "视频删除失败");
        }
    }

    //根据视频id获取视频播放凭证
    @Override
    public String getVideoPlayAuth(String videoId) {
        try {
            //通过工具类获得初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //请求
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);

            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //得到播放凭证并返回
            return response.getPlayAuth();

        } catch (ClientException e) {
            throw new GuliException(20001, "播放凭证获取失败");
        }
    }
}
