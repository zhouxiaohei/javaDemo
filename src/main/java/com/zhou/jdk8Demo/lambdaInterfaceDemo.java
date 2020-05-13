package com.zhou.jdk8Demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by 10124 on 2017/6/21.
 */
public class lambdaInterfaceDemo {
    /**
     * Consumer<T>  消费型接口 消费对象
     *     void accept(T t);
     * Supplier<T>  供给型接口 生成对象
     *     T get();
     * Function<R,T>  函数型接口 指定特定功能
     *     R apply(T t);
     * Predicate<T>  断言型接口 进行条件判断
     *     boolean test(T t);
     * */
    public static void main(String[] args) {

    }

    public void test1(){
        happy(10000, (m) -> System.out.println("小明喜欢购物，每次消费：" + m + "元"));
    }

    public void happy(double money, Consumer<Double> con){
        con.accept(money);
    }

    public void test2(){
        List<Integer> numList = getNumList(10, () -> (int)(Math.random() * 100));

        for (Integer num : numList) {
            System.out.println(num);
        }
    }
    //产生指定个数的整数，并放入集合中
    public List<Integer> getNumList(int num, Supplier<Integer> sup){
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Integer n = sup.get();
            list.add(n);
        }

        return list;
    }


    public void test3(){
        String newStr = strHandler("\t\t\t 这里是去前后空格的   ", (str) -> str.trim());
        System.out.println(newStr);

        String subStr = strHandler("这里是截取字符串的", (str) -> str.substring(3, 8));
        System.out.println(subStr);
    }

    public String strHandler(String str, Function<String, String> fun){
        return fun.apply(str);
    }

    public void test4(){
        List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "www", "ok");
        List<String> strList = filterStr(list, (s) -> s.length() > 3);

        for (String str : strList) {
            System.out.println(str);
        }
    }

    public List<String> filterStr(List<String> list, Predicate<String> pre){
        List<String> strList = new ArrayList<>();
        for (String str : list) {
            if(pre.test(str)){
                strList.add(str);
            }
        }
        return strList;
    }
}
