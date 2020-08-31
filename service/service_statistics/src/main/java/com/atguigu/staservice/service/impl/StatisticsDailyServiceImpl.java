package com.atguigu.staservice.service.impl;

import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-09
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    //生成统计数据
    @Override
    public void createStatisticsByDay(String day) {

        //为保证数据准确性每次生成统计数据都删除一下之前的数据
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);
        
        //获取统计信息
        //1、该天注册人数
        Integer registerNum = ucenterClient.countRegister(day);
        //2、该天登录人数，TODO
        Integer loginNum = RandomUtils.nextInt(100, 200);
        //2、该天视频播放量，TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);
        //2、该天添加课程量，TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);

    }

    //获取图表信息
    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        //1、获取统计数据
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type,"date_calculated");
        queryWrapper.between("date_calculated",begin,end);
        List<StatisticsDaily> dailyList = baseMapper.selectList(queryWrapper);

        //2、设置数据集合，并赋值
        //数据集合
        List<Integer> dataList = new ArrayList<>();
        //日期集合
        List<String> dateList = new ArrayList<>();
        for (StatisticsDaily daily : dailyList) {
            //向日期集合中设置值
            dateList.add(daily.getDateCalculated());
            //向数据集合中设置值
            switch (type){
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        //3、设置返回map
        Map<String,Object> map = new HashMap<>();
        map.put("dataList",dataList);
        map.put("dateList",dateList);
        return map;
    }
}
