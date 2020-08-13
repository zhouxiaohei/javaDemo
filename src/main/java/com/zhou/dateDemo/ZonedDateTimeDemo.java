package com.zhou.dateDemo;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @ClassName ZonedDateTimeDemo
 * @Author JackZhou
 * @Date 2020/7/29  18:30
 **/
public class ZonedDateTimeDemo {

    public static void main(String[] args) {
        test1();
    }

    //观察打印的两个ZonedDateTime，发现它们时区不同，但表示的时间都是同一时刻（毫秒数不同是执行语句时的时间差）：
    public static void test1(){
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        ZonedDateTime zny = ZonedDateTime.now(ZoneId.of("America/New_York")); // 用指定时区获取当前时间
        System.out.println(zbj);
        System.out.println(zny);

//        LocalDateTime ldt = LocalDateTime.of(2019, 9, 15, 15, 16, 17);
//        ZonedDateTime zbj = ldt.atZone(ZoneId.systemDefault());
//        ZonedDateTime zny = ldt.atZone(ZoneId.of("America/New_York"));
//        System.out.println(zbj);
//        System.out.println(zny);
    }

    //时区转换  要特别注意，时区转换的时候，由于夏令时的存在，不同的日期转换的结果很可能是不同的
    //https://www.liaoxuefeng.com/wiki/1252599548343744/1303904694304801
    public static void test2(){
        // 以中国时区获取当前时间:
        ZonedDateTime zbj = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 转换为纽约时间:
        ZonedDateTime zny = zbj.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(zbj);
        System.out.println(zny);
    }


}
