package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.entity.vo.UserInfo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2022-04-10
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 用户登录
     *
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验登录参数是否合法
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "信息不全");
        }

        //校验用户是否存在
        UcenterMember ucenterMember = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (null == ucenterMember) {
            throw new GuliException(20001, "用户不存在");
        }

        //校验密码
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())) {
            throw new GuliException(20001, "密码错误");
        }

        //判断是否被禁用
        if (ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"改用户已被禁用");
        }

        //使用JWT生成token字符串，并返回token值
        return JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
    }

    /**
     * 用户注册
     *
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        //校验注册信息是否合法
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "信息不全");
        }

        //校验验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }

        //校验手机号码是否重复
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (count >0){
            throw new GuliException(20001,"手机号码已存在");
        }

        //保存用户信息
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://wshuai.oss-cn-beijing.aliyuncs.com/2020/05/24/4cd3e621d66b4e21bf861f0ec7e5df44file.png");//设置默认头像
        baseMapper.insert(member);

    }

    //根据用户id获取用户信息
    @Override
    public UserInfo getUserInfo(String memberId) {
        if (memberId!=null && !memberId.isEmpty()){
            UcenterMember member = baseMapper.selectById(memberId);
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(member,userInfo);
            return userInfo;
        }
        return null;
    }

    //根据用户Openid获取用户信息
    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        return baseMapper.selectOne(queryWrapper);
    }

    //查询某一天的注册人数
    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }


}
