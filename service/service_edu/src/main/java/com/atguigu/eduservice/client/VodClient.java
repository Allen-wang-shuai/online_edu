package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.impl.VodFileDegradeFeignClient;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * service-vod服务使用接口
 * 指定熔断器实现类，方法执行超时时或者执行出错时熔断器实现类中的方法
 */
@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    //根据视频id删除视频，注意这里@PathVariable里面一定要指定参数名称
    @DeleteMapping("/eduvod/video/{videoId}")
    public R removeVideoById(@PathVariable("videoId") String videoId);

    //根据视频id批量删除视频
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);

}
