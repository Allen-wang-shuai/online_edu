package com.atguigu.staservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2022-04-05
 */
@Api(description = "网站统计日数据")
@RestController
@RequestMapping("/staservice/sta")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    //生成统计数据
    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day){
        staService.createStatisticsByDay(day);
        return R.ok();
    }

    //获取图表信息
    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String,Object> map = staService.getChartData(begin,end,type);
        return R.ok().data(map);
    }

}

