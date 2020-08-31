package com.atguigu.eduservice.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 当VodClient中的方法执行超时时或者执行出错时执行这里的方法
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R removeVideoById(String videoId) {
        return R.error().message("Time Out!");
    }

    @Override
    public R removeVideoList(List<String> videoIdList) {
        return R.error().message("Time Out!");
    }
}
