package com.atguigu.staservice.client;

import com.atguigu.staservice.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {

    @GetMapping("/api/ucenter/countRegister/{day}")
    public Integer countRegister(@PathVariable("day") String day);

}
