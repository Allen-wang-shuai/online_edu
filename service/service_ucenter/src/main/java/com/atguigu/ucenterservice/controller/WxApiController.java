package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.ConstantWxUtils;
import com.atguigu.ucenterservice.utils.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 微信登录控制类
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UcenterMemberService memberService;

    //1、获取登录二维码
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        // 设置微信开放平台授权baseUrl，%s相当于占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），
         * 建议第三方带上该参数，可设置为简单的随机数加session进行校验
         */
        //String state = "atguigu";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        String state = UUID.randomUUID().toString().replaceAll("-", "");
        // 采用redis等进行缓存state 使用sessionId为key 3分钟后过期，可配置
        //键："wechart-open-state-" + httpServletRequest.getSession().getId()
        String key = "wechart-open-state-" + session.getId();
        //值：state
        //存入redis
        redisTemplate.opsForValue().set(key, state, 3, TimeUnit.MINUTES);

        /**
         * 生成qrcodeUrl
         * 第一个参数：要进行拼接的URL
         * 第二、三、四个参数：往%s中填充的数据
         */
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        return "redirect:" + qrcodeUrl;
    }

    //2、获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        //1、从redis中将state获取出来，和当前传入的state作比较，
        // 如果一致则放行，如果不一致则抛出异常：非法访问，授权超时
        String key = "wechart-open-state-" + session.getId();
        String redisState = redisTemplate.opsForValue().get(key);
        if (!(redisState != null && redisState.equals(state))) {
            throw new GuliException(20001, "授权超时");
        }

        //2、向认证服务器发送请求获取access_token
        //拼接请求字符串
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        //向字符串中赋值
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code);
        //发送请求
        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }

        //3、解析JSON字符串，获取accessToken和openid
        Gson gson = new Gson();
        HashMap hashMap = gson.fromJson(result, HashMap.class);
        String accessToken = (String) hashMap.get("access_token");
        String openid = (String) hashMap.get("openid");

        //3、查询数据库当前用户是否曾经使用过微信登录
        UcenterMember member = memberService.getByOpenid(openid);
        if (member == null) {
            //第一次登录
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                throw new GuliException(20001, "获取用户信息失败");
            }

            //解析json
            HashMap mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname");
            String headimgurl = (String)mapUserInfo.get("headimgurl");

            //向数据库中插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }

        //完成登录
        //生成jwt
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
        //因为端口号不同存在跨域问题，cookie不能跨域，所以这里使用url重写将token返回实现登录
        return "redirect:http://localhost:3000?token="+token;
    }

}
