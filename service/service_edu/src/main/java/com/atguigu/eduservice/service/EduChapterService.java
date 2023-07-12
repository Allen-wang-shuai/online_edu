package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
public interface EduChapterService extends IService<EduChapter> {

    //根据课程Id返回一个课程的所有章节信息
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节
    boolean deleteChapter(String chapterId);

    void removeByCourseId(String courseId);
}
