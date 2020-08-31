package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，
        // 请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();

            //**上传完善**
            //1、为了避免文件名称一样造成之前的文件被后来的文件覆盖我们在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;

            //2、将日期作为文件路径2020/05/16/01.jpg
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //文件及其路径拼接
            fileName = datePath+"/"+fileName;

            //调用oss的方法实现上传
            //第一个参数 bucket名称
            //第二个参数 上传到oss文件路径和文件名称 例：/aa/bb/1.jpg
            //第三个参数 上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传后文件路径返回，没有对应方法需要我们手动拼接
            //https://wshuai.oss-cn-beijing.aliyuncs.com/wangshuai%20%282%29.JPG
            return "https://"+bucketName+"."+endpoint+"/"+fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
