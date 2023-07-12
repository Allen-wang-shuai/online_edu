package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);

    boolean removeVideoById(String videoId);
}
