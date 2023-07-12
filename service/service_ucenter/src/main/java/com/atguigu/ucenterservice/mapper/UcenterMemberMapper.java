package com.atguigu.ucenterservice.mapper;

import com.atguigu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author wangshuai
 * @since 2022-04-10
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某一天的注册人数
    Integer countRegister(String day);
}
