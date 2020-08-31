package com.atguigu.staservice.config;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

//    /**
//     * 测试
//     * 每天七点到二十三点每五秒执行一次
//     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1() {
//        System.out.println("*********++++++++++++*****执行了");
//    }

    /**
     * 每天凌晨1点执行定时
     * 注意在SpringBoot中cron表达式只能写六位，写七位会出错，年默认时当前年
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.createStatisticsByDay(day);
    }

}
