package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoData> {

    //一行一行读取excel的内容，不会读取表头
    @Override
    public void invoke(DemoData data, AnalysisContext analysisContext) {
        System.out.println("***"+data+"***");
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    //读取完成之后调用该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


}
