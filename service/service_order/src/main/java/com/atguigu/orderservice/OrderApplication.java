package com.atguigu.orderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients//声明为服务调用端
@EnableDiscoveryClient//向注册中心注册
@SpringBootApplication
@ComponentScan("com.atguigu")
@MapperScan("com.atguigu.orderservice.mapper")//扫描mapper包，使编译时能编译xml文件
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
