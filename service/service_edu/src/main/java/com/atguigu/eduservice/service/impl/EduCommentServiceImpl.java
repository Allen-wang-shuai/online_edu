package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-04
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //根据课程id分页查询评论信息
    @Override
    public Map<String, Object> getCourseCommentInfo(Long page, Long limit, String courseId) {
        //创建分页参数
        Page<EduComment> pageParam = new Page<>(page,limit);
        //创建查询条件
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        //执行查询
        baseMapper.selectPage(pageParam,queryWrapper);
        //封装参数
        List<EduComment> records = pageParam.getRecords();
        Map<String,Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",pageParam.getCurrent());
        map.put("pages",pageParam.getPages());
        map.put("size",pageParam.getSize());
        map.put("total",pageParam.getTotal());
        map.put("hasNext",pageParam.hasNext());
        map.put("hasPrevious",pageParam.hasPrevious());

        return map;
    }
}
