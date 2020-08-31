package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.util.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-05
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成支付二维码信息
    @Override
    public Map<String, Object> createNative(String orderNo) {
        try {
            //1、根据订单编号获取订单信息
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(queryWrapper);

            //2、设置支付参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");//商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符串
            m.put("body", order.getCourseTitle());//设置课程名称
            m.put("out_trade_no", orderNo);//二维码唯一标识，这里用订单号码
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");//设置支付金额，固定写法
            m.put("spbill_create_ip", "127.0.0.1");//服务所在ip
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");//回调地址
            m.put("trade_type", "NATIVE");//支付类型

            //3、向微信URL中传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置参数，这里是XML格式，这里还需要设置商户key用于加密
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            //允许https
            client.setHttps(true);
            //发送请求
            client.post();

            //4、获得并封装返回数据，返回数据格式是XML格式的
            String xml = client.getContent();
            Map<String, String> responseMap = WXPayUtil.xmlToMap(xml);
            //封装返回结果集
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("out_trade_no", orderNo);
            resultMap.put("course_id", order.getCourseId());
            resultMap.put("total_fee", order.getTotalFee());
            resultMap.put("result_code", responseMap.get("result_code"));//返回的状态码，200之类的
            resultMap.put("code_url", responseMap.get("code_url"));//二维码地址

            //5、返回数据
            return resultMap;
        } catch (Exception e) {
            throw new GuliException(20001, "生成支付二维码信息失败");
        }
    }

    //根据订单编号获取支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");//商户号
            m.put("out_trade_no", orderNo);//订单编号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符串

            //2、向微信URL中传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //设置参数，这里是XML格式，这里还需要设置商户key用于加密
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            //允许https
            client.setHttps(true);
            //发送请求
            client.post();

            //3、获得第三方数据，转成map并返回，里面含有支付状态trade_state
            String xml = client.getContent();
            return WXPayUtil.xmlToMap(xml);

        } catch (Exception e) {
            //发生异常的话返回null
            return null;
        }
    }

    //更改订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

//        System.out.println("订单编号："+orderNo+"支付状态："+order.getStatus());

        //如果状态为1证明已支付，不需要支付了直接返回即可
        if(order.getStatus() == 1) return;
        //否则将状态修改为1
        order.setStatus(1);
        orderService.updateById(order);
        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));//支付的其它属性
        baseMapper.insert(payLog);//插入到支付日志表
    }
}
