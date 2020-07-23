package com.zhou.regexDemo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName RegexDemo
 * @Author JackZhou
 * @Date 2019/12/26  17:14
 **/
public class RegexDemo {

    public static void main(String[] args) {
        test2();
    }

    public static void test3() {
        String regex = "\\$\\{(.*?)\\}";
        // 把规则编译成模式对象
        Pattern p = Pattern.compile(regex);

        String str = "${$.name},${$.value},${$.size}";
        // 通过模式对象得到匹配器对象
        Matcher m = p.matcher(str);
        // 调用匹配器对象的功能
        // 通过find方法就是查找有没有满足条件的子串

        while (m.find()) {
            // 取值
            System.out.println(m.group());
        }
    }

    public static void test(){
        String content = "I am noob " +
                "from runoob.com.";

        String pattern = ".*runoob.*";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);
    }

    public static void test2(){
        String content = "/demo/serviced/exDemo/1234";
        String pattern = ".*/exDemo/.*";
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含: " + isMatch);
    }

}
