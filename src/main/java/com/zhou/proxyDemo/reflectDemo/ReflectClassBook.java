package com.zhou.proxyDemo.reflectDemo;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName ReflectClassBook
 * @Author JackZhou   参考 https://www.jianshu.com/p/9be58ee20dee
 * @Date 2021/2/11  22:05
 **/
@Slf4j
public class ReflectClassBook {

    public static void main(String[] args) {
        reflectNewInstance();
    }

    // 创建对象
    public static void reflectNewInstance() {
        try {
            Class<?> classBook = Class.forName("com.zhou.proxyDemo.reflectDemo.Book");
            Object objectBook = classBook.newInstance();
            Book book = (Book) objectBook;
            book.setName("Android进阶之光");
            book.setAuthor("刘望舒");
            log.info("reflectNewInstance book = " + book.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有的构造方法
    public static void reflectPrivateConstructor() {
        try {
            Class<?> classBook = Class.forName("com.zhou.proxyDemo.reflectDemo.Book");
            Constructor<?> declaredConstructorBook = classBook.getDeclaredConstructor(String.class,String.class);
            declaredConstructorBook.setAccessible(true);
            Object objectBook = declaredConstructorBook.newInstance("Android开发艺术探索","任玉刚");
            Book book = (Book) objectBook;
            log.info("reflectPrivateConstructor book = " + book.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有属性
    public static void reflectPrivateField() {
        try {
            Class<?> classBook = Class.forName("com.zhou.proxyDemo.reflectDemo.Book");
            Object objectBook = classBook.newInstance();
            Field fieldTag = classBook.getDeclaredField("TAG");
            fieldTag.setAccessible(true);
            String tag = (String) fieldTag.get(objectBook);
            log.info("reflectPrivateField tag = " + tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 反射私有方法
    public static void reflectPrivateMethod() {
        try {
            Class<?> classBook = Class.forName("com.zhou.proxyDemo.reflectDemo.Book");
            Method methodBook = classBook.getDeclaredMethod("declaredMethod",int.class);
            methodBook.setAccessible(true);
            Object objectBook = classBook.newInstance();
            String string = (String) methodBook.invoke(objectBook,0);

            log.info("reflectPrivateMethod string = " + string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
