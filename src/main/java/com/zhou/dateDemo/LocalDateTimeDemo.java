package com.zhou.dateDemo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @ClassName LocalDateTimeDemo
 * @Author JackZhou
 * @Date 2020/7/29  17:55
 *  https://www.liaoxuefeng.com/wiki/1252599548343744/1303871087444002
 * 从Java 8开始，java.time包提供了新的日期和时间API，主要涉及的类型有：
 *
 * 本地日期和时间：LocalDateTime，LocalDate，LocalTime；
 * 带时区的日期和时间：ZonedDateTime；
 * 时刻：Instant；
 * 时区：ZoneId，ZoneOffset；
 * 时间间隔：Duration。
 * 以及一套新的用于取代SimpleDateFormat的格式化类型DateTimeFormatter。
 *
 * 和旧的API相比，新API严格区分了时刻、本地日期、本地时间和带时区的日期时间，并且，对日期和时间进行运算更加方便。
 *
 * 此外，新API修正了旧API不合理的常量设计：
 *
 * Month的范围用1~12表示1月到12月；
 * Week的范围用1~7表示周一到周日。
 * 最后，新API的类型几乎全部是不变类型（和String类似），可以放心使用不必担心被修改。
 *
 **/
public class LocalDateTimeDemo {

    // 注意ISO 8601规定的日期和时间分隔符是T。标准格式如下：
    //
    //日期：yyyy-MM-dd
    //时间：HH:mm:ss
    //带毫秒的时间：HH:mm:ss.SSS
    //日期和时间：yyyy-MM-dd'T'HH:mm:ss
    //带毫秒的日期和时间：yyyy-MM-dd'T'HH:mm:ss.SSS
    public static void main(String[] args) {
        //test1();
        test3();
    }

    // 简单使用LocalDate、LocalTime、LocalDateTime
    public static void test1(){
        LocalDate d = LocalDate.now(); // 当前日期
        LocalTime t = LocalTime.now(); // 当前时间
        LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
        System.out.println(d); // 严格按照ISO 8601格式打印
        System.out.println(t); // 严格按照ISO 8601格式打印
        System.out.println(dt); // 严格按照ISO 8601格式打印

        // 如果毫秒值对不上，可以换成如下格式
//        LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
//        LocalDate d = dt.toLocalDate(); // 转换到当前日期
//        LocalTime t = dt.toLocalTime(); // 转换到当前时间
    }

    //指定日期和时间转成LocalDate、LocalTime、LocalDateTime对象
    public static void test2(){
        // 指定日期和时间:
        LocalDate d2 = LocalDate.of(2019, 11, 30); // 2019-11-30, 注意11=11月
        LocalTime t2 = LocalTime.of(15, 16, 17); // 15:16:17
        LocalDateTime dt2 = LocalDateTime.of(2019, 11, 30, 15, 16, 17);
        LocalDateTime dt3 = LocalDateTime.of(d2, t2);

        LocalDateTime dt = LocalDateTime.parse("2019-11-19T15:16:17");
        LocalDate d = LocalDate.parse("2019-11-19");
        LocalTime t = LocalTime.parse("15:16:17");
    }

    //默认的LocalDateTime 时间和日期的间隔是T，如果要做格式转换  SimpleDateFormat升级为DateTimeFormatter。
    //SimpleDateFormat不同的是，DateTimeFormatter不但是不变对象，它还是线程安全的。
    //SimpleDateFormat不是线程安全的
    public static void test3(){
        // 自定义格式化:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));

        // 用自定义格式解析:
        LocalDateTime dt2 = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
        System.out.println(dt2);
    }

    // 对时间进行加减操作
    //注意到月份加减会自动调整日期，例如从2019-10-31减去1个月得到的结果是2019-09-30，因为9月没有31日。
    //对日期和时间进行调整则使用withXxx()方法，例如：withHour(15)会把10:11:12变为15:11:12：
    public static void test4(){
        LocalDateTime dt = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
        System.out.println(dt);
        // 加5天减3小时:
        LocalDateTime dt2 = dt.plusDays(5).minusHours(3);
        System.out.println(dt2); // 2019-10-31T17:30:59
        // 减1月:
        LocalDateTime dt3 = dt2.minusMonths(1);
        System.out.println(dt3); // 2019-09-30T17:30:59

//        LocalDateTime dt = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
//        System.out.println(dt);
//        // 日期变为31日:
//        LocalDateTime dt2 = dt.withDayOfMonth(31);
//        System.out.println(dt2); // 2019-10-31T20:30:59
//        // 月份变为9:
//        LocalDateTime dt3 = dt2.withMonth(9);
//        System.out.println(dt3); // 2019-09-30T20:30:59
    }

    public static void test5(){
        // 本月第一天0:00时刻:
        LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        System.out.println(firstDay);

        // 本月最后1天:
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(lastDay);

        // 下月第1天:
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println(nextMonthFirstDay);

        // 本月第1个周一:
        LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println(firstWeekday);
    }



}
