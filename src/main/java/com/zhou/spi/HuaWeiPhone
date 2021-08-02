package com.zhou.spi;

import java.util.ServiceLoader;

/**
 * @ClassName HuaWeiPhone
 * @Author JackZhou
 * @Date 2021/6/25  11:03
 **/
public class HuaWeiPhone implements Phone {
    public void say() {
        System.out.println("I am Hua Wei");
    }

    public static void main(String[] args) {
        ServiceLoader<Phone> shouts = ServiceLoader.load(Phone.class);
        for (Phone impl : shouts) {
            impl.say();
        }
    }
}
