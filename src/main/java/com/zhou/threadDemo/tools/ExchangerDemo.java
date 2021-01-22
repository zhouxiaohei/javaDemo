package com.zhou.threadDemo.tools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ExchangerDemo
 * @Author JackZhou
 * @Date 2020/12/3  19:56
 **/
public class ExchangerDemo {

    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger<>();
        Thread grilThread = new Thread(() -> {
            try {
                String gril = (String) exchanger.exchange("我暗恋你很久了...");
                System.out.println("⼥⽣说:" + gril);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        grilThread.start();
        Thread boyThread = new Thread(() -> {
            try {
                System.out.println("⼥神慢慢地从教室⾥⾛出来...");
                TimeUnit.SECONDS.sleep(2);
                String boy = (String) exchanger.exchange("我喜欢你!");
                System.out.println("男⽣说:" + boy);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        boyThread.start();
    }
}
