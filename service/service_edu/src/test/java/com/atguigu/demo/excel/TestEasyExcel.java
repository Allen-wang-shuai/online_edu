package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        //EasyExcel写操作
        //重复执行会覆盖之前写入的数据
        //1.设置写入文件夹地址和excel文件名称
        String filename = "E:\\write.xlsx";

        //2.调用easyexcel里面的方法实现写操作
        //write方法两个参数：第一个参数文件路径名，第二个参数实体类class
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

    }

    //测试EasyExcel的读操作
    @Test
    public void testReadExcel(){

        //1.设置写入文件夹地址和excel文件名称
        String filename = "E:\\write.xlsx";

        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();

    }

    //创建方法返回list集合
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        //java集合存储对象时存储的是对象的引用，也就是该对象的地址，通过该地址指向该对象，一个地址对应一个对象
        //所以这里的DemoData必须在循环里面创建对象，如果创建到外面因为集合存储的是对象的引用的缘故到最后所有
        //元素的值都是一样的，这里需要注意！！！
        for (int i=0;i<10;i++){
            DemoData data = new DemoData();
            data.setSno(201800+i);
            data.setSname("student"+i);
            list.add(data);
        }
        return list;
    }

}
