package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2022-03-19
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    //根据课程Id返回一个课程的所有章节信息
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1、根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.orderByAsc("sort");
        chapterQueryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(chapterQueryWrapper);

        //2、根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.orderByAsc("sort");
        videoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(videoQueryWrapper);

        //创建list集合，用于封装最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3、遍历查询章节list集合进行封装
        for (EduChapter eduChapter : eduChapterList) {
            //把eduChapter里面的值取出来放到ChapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapterVo放到finalList中
            finalList.add(chapterVo);

            //4、在章节中遍历查询所有的小节，将对应的小节加入到其所属的章节中
            //创建list集合存放小节
            List<VideoVo> videoVos = new ArrayList<>();
            for (EduVideo eduVideo : eduVideoList) {
                //判断小节中的chapter_id和目前的章节id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //一样的话把eduVideo里面的对应信息复制到videoVo中，并把videoVo放到videoVos中
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVos.add(videoVo);
                }
            }

            //把章节中的小节信息加入到章节当中
            chapterVo.setChildren(videoVos);
        }

        //返回最终章节信息
        return finalList;
    }

    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {

        //根据chapterId查询小节表，如果该章节下存在小节则不进行删除
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",chapterId);
        int count = videoService.count(videoQueryWrapper);
        if (count>0){//count>0表示该章节下有小节不可以删除
            throw new GuliException(20001,"该章节下存在小节，不能删除");
        }else {
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }

    }

    //根据课程id删除章节
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }


}
