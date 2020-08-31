package com.atguigu.canal;

import java.util.*;

public class Test {

    String str = "全局变量";

    public static void main(String[] args) {
        System.out.println(10 / 3);
        System.out.println(Math.pow(2, 3));
        System.out.println("hello" + " world");
        System.out.println("hello");
        int x = 1;
        x = x + 2;
        System.out.println(x);
        int a, b, c, d, e, f;
        a = b = c = d = e = f = 10;
        System.out.println(f);
        System.out.println(3 >= 2 ? '对' : '错');
        System.out.println('a' > 80);
        System.out.println('a' == 97);
        System.out.println(1 > 2 || 2 < 3);
        System.out.println(-7 >> 1);

        String m = "hello";
        String replace = m.replace('l', 'x');
        System.out.println(m);
        System.out.println(replace);
        String y = "d,f,f,a,q,t,t,g,a,h";
        char[] chars = y.toCharArray();
        String s2 = Arrays.toString(chars);
        System.out.println(s2);
        String[] split = y.split(",");
        String s1 = Arrays.toString(split);
        System.out.println(s1);
        String s = split.toString();
        System.out.println(s);
        List<String> list = Arrays.asList(split);
        System.out.println(list);
        System.out.println("----------");
        int[] arr = new int[10];
        arr[0] = 1;
        arr[0] = 10;
        for (int i : arr) {
            System.out.println(i);
        }
        Map<String, Object> he = new HashMap<>();
        he.put("name", "wangshuai");
        String gender = (String) he.get("gender");
        if (gender == null) {
            System.out.println("当没有在map集合中找到key时返回Null");
        }

        String str = "局部变量";
        System.out.println(str);

        String k = "abc";
        String j = "abc";
        System.out.println(k == j);
    }

    void speak() {
        System.out.println(str);
    }

}
