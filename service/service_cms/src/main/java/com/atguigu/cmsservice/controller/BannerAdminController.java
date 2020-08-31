package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 后台控制器
 * </p>
 *
 * @author wangshuai
 * @since 2020-06-08
 */
@Api(description = "后端幻灯片管理")
@RestController
@RequestMapping("/cmsservice/banneradmin")
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //1.获取banner分页列表
    @ApiOperation(value = "获取banner分页列表")
    @GetMapping("{page}/{limit}")
    public R pageBanner(@ApiParam(name = "page",value = "当前页码") @PathVariable Long page,
                        @ApiParam(name = "limit",value = "每页记录数") @PathVariable Long limit){
        Page<CrmBanner> pageParam = new Page<>(page,limit);
        bannerService.page(pageParam,null);
        return R.ok().data("items",pageParam.getRecords()).data("total",pageParam.getTotal());
    }

    //2.根据id获取banner
    @ApiOperation(value = "根据id获取banner")
    @GetMapping("get/{id}")
    public R getBanner(@PathVariable String id){
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item",banner);
    }

    //3.新增banner
    @ApiOperation(value = "新增banner")
    @PostMapping("save")
    public R save(@RequestBody CrmBanner banner){
        bannerService.save(banner);
        return R.ok();
    }

    //4.根据id修改banner
    @ApiOperation(value = "修改banner")
    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return R.ok();
    }

    //5.删除banner
    @ApiOperation(value = "删除banner")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }

}

