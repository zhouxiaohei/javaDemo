package com.zhou.dateDemo;

import java.time.Instant;

/**
 * @ClassName InstantDemo
 * @Description
 * @Author JackZhou
 * @Date 2020/7/29  18:19
 **/

// https://www.jianshu.com/p/3fbe607600a5    System.currentTimeMillis()的性能问题
public class InstantDemo {

    //Instant 对性能问题有改善么
    public static void test1(){

        System.currentTimeMillis();

        Instant now = Instant.now();
        System.out.println(now.getEpochSecond()); // 秒
        System.out.println(now.toEpochMilli()); // 毫秒
    }


}
