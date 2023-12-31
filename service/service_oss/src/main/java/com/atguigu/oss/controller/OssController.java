package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "OSS对象存储管理")
@RestController
@RequestMapping("/eduoss/fileoss")
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像的方法
    @ApiOperation(value = "文件上传")
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //获取上传文件MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }

}
