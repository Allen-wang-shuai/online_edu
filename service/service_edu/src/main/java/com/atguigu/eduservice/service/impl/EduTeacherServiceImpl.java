package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 * 这里继承了ServiceImpl这个类已经帮我们注入了Mapper，我们不需要再注入该service对应的Mapper了
 * @author wangshuai
 * @since 2022-03-19
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //分页条件查询
    @Override
    public void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery) {

        //创建查询条件对象
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        if (teacherQuery==null){
            baseMapper.selectPage(page,queryWrapper);
            return;
        }

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if (level!=null){
            queryWrapper.eq("level",level);
        }

        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }

        baseMapper.selectPage(page,queryWrapper);

    }

    //得到4个首页教师信息，添加缓存
    @Cacheable(value = "index",key = "'get4Teacher'")
    @Override
    public List<EduTeacher> get4Teacher(QueryWrapper<EduTeacher> wrapperTeacher) {
        return baseMapper.selectList(wrapperTeacher);
    }

    //分页查询讲师
    @Override
    public Map<String, Object> getTeacherPageList(Page<EduTeacher> pageParam) {
        //创建查询条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        //执行查询
        baseMapper.selectPage(pageParam,queryWrapper);

        //处理分页数据
        List<EduTeacher> records = pageParam.getRecords();
        //当前页
        long current = pageParam.getCurrent();
        //共多少页
        long pages = pageParam.getPages();
        //每页记录数
        long size = pageParam.getSize();
        //总条数
        long total = pageParam.getTotal();
        //是否有下一页
        boolean hasNext = pageParam.hasNext();
        //是否有上一页
        boolean hasPrevious = pageParam.hasPrevious();

        //封装数据
        Map<String,Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        //返回数据
        return map;
    }

}
