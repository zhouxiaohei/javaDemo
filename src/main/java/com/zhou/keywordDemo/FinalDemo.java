package com.zhou.keywordDemo;

/**
 * @ClassName FinalDemo
 * @Author JackZhou
 * @Date 2021/1/26  14:08
 *
 *   如果引用为基本数据类型，则该引用为常量，该值无法修改；
 *   如果引用为引用数据类型，比如对象、数组，则该对象、数组本身可以修改，但指向该对象或数组的地址的引用不能修改。
 *   如果引用时类的成员变量，则必须当场赋值，否则编译会报错。
 *   https://www.cnblogs.com/chhyan-dream/p/10685878.html
 *
 *   final的重排序规则
 *
 *   对于final域，编译器和处理器要遵守两个重排序规则。
 *
 *  在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
 *  初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能重排序。
 **/
public class FinalDemo {
    public static void main(String[] args) {
        //1. 基本数组类型为常量，无法修改
        final int i = 9;
        //i = 10;

        //2. 地址不能修改，但是对象本身的属性可以修改
        Person p = new Person();
        p.name = "lisi";
        final int[] arr = {1,2,3,45};
        arr[3] = 999;
        //arr = new int[]{1,4,56,78};
    }

}

final class Person {
    String name ="zs";
    //3、此处不赋值会报错
    //final int age;
    final int age = 10;

    public final void say() {
        System.out.println("说....");
    }
    public void eat() {
        System.out.println("吃...");
    }
}

