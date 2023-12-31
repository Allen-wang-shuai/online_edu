package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestVod {

    //1、根据视频id获取视频播放地址
    @Test
    public void getVideoUrl() throws ClientException {
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GCsRxcZko18AhkQajAy", "qqPyievkA1NX1XOwrMb2fVNnoMeKDR");

        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId("fb2b59016bbb4cf5abc8cc4d93543d99");

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }

    //2、根据视频id获取视频播放凭证
    @Test
    public void getVideoAuth() throws ClientException {

        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GCsRxcZko18AhkQajAy", "qqPyievkA1NX1XOwrMb2fVNnoMeKDR");

        //创建获取视频播放凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //向request对象里面设置视频id
        request.setVideoId("fb2b59016bbb4cf5abc8cc4d93543d99");
//        request.setVideoId("a7b254634cdd4f6bbf153242ae1e3b74");未加密的视频也可以获得凭证

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);
        //播放凭证，注意后面的两个==不要复制！！！！！！
        System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
        //VideoMeta信息
        System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");

    }

    //3、根据视频id删除视频
    @Test
    public void deleteVideo() throws ClientException {

        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GCsRxcZko18AhkQajAy", "qqPyievkA1NX1XOwrMb2fVNnoMeKDR");

        //创建删除视频的request和response
        DeleteVideoRequest request = new DeleteVideoRequest();
        DeleteVideoResponse response = new DeleteVideoResponse();

        //向request对象里面设置视频id，注意这个可以设置多个视频id，id之间用逗号隔开
        request.setVideoIds("092b8db673004bb1a715dc94a681c262");

        //调用初始化对象里面的方法，传递request，删除视频
        response = client.getAcsResponse(request);

    }

    //4、视频上传
    @Test
    public void uploadVideo() {
        String accessKeyId = "LTAI4GCsRxcZko18AhkQajAy";
        String accessKeySecret = "qqPyievkA1NX1XOwrMb2fVNnoMeKDR";
        String title = "uploadVideo";    //上传文件的名称
        String fileName = "E:\\BaiduNetdiskDownload\\谷粒学院项目资料\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4"; //本地文件路径和名称

        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();

        //视频上传
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID

        //是否有回调返回值
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    //测试join方法
//    @Test
//    public void test(){
//        List<String> list = new ArrayList<>();
//        list.add("nihao");
//        list.add("wohao");
//        list.add("dajiahao");
//        String join = StringUtils.join(list, ",");
//        System.out.println(join);
//    }

}
