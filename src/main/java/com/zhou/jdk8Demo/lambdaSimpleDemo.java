package com.zhou.jdk8Demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 10124 on 2017/6/21.
 */
public class lambdaSimpleDemo {

    /**
     * new code
     */
    /**
     * 1、
     * Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
         });
     2、
     Collections.sort(names, (String a, String b) -> b.compareTo(a));
     3、
     Collections.sort(names, (a, b) -> b.compareTo(a));
     */

    /**
     * Old code
     */
    public static void main(String[] args) {
        //策略模式：中间过程类似、结果类似  使用方式不一样。 使用接口、实现接口或者内部类方式完成特定策略。

        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names, new Comparator<String>() {//java以前老版本的写法
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });
        for(String name:names){
            System.out.println(name);
        }
    }

    public static void testSimple(){
        Converter<String, Integer> a = (from) -> Integer.valueOf(from);

        //Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用
        Converter<String, Integer> converter = Integer::valueOf;//这个是静态方式导入

        Integer m=a.convert("123");
        System.out.println(m);
        test test=mmm -> "aaaa";//mmm代表你要传入的参数
        String testResult=test.aa("");//aa代表你要传入的方法
        System.out.println(testResult);
    }

//使用函数式接口时，接口的定义只能有一个抽象方法
    @FunctionalInterface
    interface Converter<F, T> {
        T convert(F from);
    }

    interface test {
        String aa(String mmm);
    }

}
