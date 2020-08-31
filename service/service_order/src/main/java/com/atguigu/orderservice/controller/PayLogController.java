package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.PayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-07-05
 */
@Api(description = "支付日志控制")
@RestController
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成支付二维码信息
    @ApiOperation(value = "生成支付二维码信息")
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //二维码信息map集合
        Map<String,Object> map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    //根据订单编号获取支付状态
    @ApiOperation(value = "根据订单编号获取支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
//        System.out.println("订单编号："+orderNo);
        //调用查询接口，查询订单状态的map集合
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        //支付失败
        if (map == null){
            return R.error().message("支付失败！");
        }
        //支付成功，trade_state是微信自带的参数
        if (map.get("trade_state").equals("SUCCESS")){
            //更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功！");
        }
        System.out.println("支付中");
        //trade_state如果不是SUCCESS，则表示正在支付
        return R.ok().code(25000).message("支付中");
    }

}

