package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2020-05-19
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    //根据课程id删除小节所有信息
    @Override
    public void removeByCourseId(String courseId) {
        //根据课程id查询所有视频列表
        QueryWrapper<EduVideo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        queryWrapper1.select("video_source_id");
        List<EduVideo> videoList = baseMapper.selectList(queryWrapper1);

        //得到所有视频列表的视频id集合
        List<String> videoSourceIdList = new ArrayList<>();
        for (EduVideo eduVideo : videoList) {
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!videoSourceId.isEmpty()) {
                videoSourceIdList.add(videoSourceId);
            }
        }

        //调用vod服务删除远程视频
        if (videoSourceIdList.size() > 0) {
            vodClient.removeVideoList(videoSourceIdList);
        }

        //删除小节数据库信息
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }

    //根据videoId删除小节信息
    @Override
    public boolean removeVideoById(String videoId) {
        //查询视频id
        EduVideo eduVideo = baseMapper.selectById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();

        //根据视频id远程调用删除小节视频资源
        if (!StringUtils.isEmpty(videoSourceId)) {
            R r = vodClient.removeVideoById(videoSourceId);
            if (r.getCode()==20001){
                throw new GuliException(20001,"熔断器触发，视频删除失败");
            }
        }

        //删除小节信息
        int i = baseMapper.deleteById(videoId);

        return i > 0;
    }
}
