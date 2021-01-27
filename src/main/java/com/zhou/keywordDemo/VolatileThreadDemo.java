package com.zhou.keywordDemo;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName VolatileThreadDemo
 * @Author JackZhou
 * @Date 2020/10/28  15:10
 *
 *  1、线程可见性
 * 2、指令重排序 ---cpu乱序执行
 *    1、验证代码
 *    2、DCL(双重检查问题)问题、半初始化对象、使用对象出现问题
 * 指令重排原理：
 *    1、 voliatlie可见性
 *     2、JVM层面内存屏障，相关规范
 *     3、CPU层面lock、锁总线(有相关指令操作，考虑一致性问题和同步一样用锁)
 **/
@Slf4j
public class VolatileThreadDemo {

    // https://blog.csdn.net/ren421259121/article/details/90447853   通过测试可知线程共享变量还是需要加上volatile
    private static volatile boolean flag1 = false;

    private static boolean flag2 = false;

    private boolean flag3 = false;

    public static void main(String[] args) {

        // 加了volatile 不会异常终止
//        for(int i = 0; i< 100 ; i ++){
//            flag1 = false;
//            new VolatileThreadDemo().testFlag1();
//        }

        // 线程异常终止了
//        for(int i = 0; i< 100 ; i ++){
//            flag2 = false;
//            new VolatileThreadDemo().testFlag2();
//        }

        // 线程异常终止了
        for(int i = 0; i< 10 ; i ++){
            new VolatileThreadDemo().testFlag3();
        }
    }

    public void testFlag1(){
        new Thread( () -> {
            log.info("修改flag的值为true");
            flag1 = true;
        }).start();
        long startTime = System.currentTimeMillis();
        while(!flag1){
            long currentTime = System.currentTimeMillis();
            if(currentTime - startTime > 10000){
                log.error("线程异常终止");
                break;
            }
        }
        log.info("线程运行终止");
    }

    public void testFlag2(){
        new Thread( () -> {
            log.info("修改flag的值为true");
            flag2 = true;
        }).start();
        long startTime = System.currentTimeMillis();
        while(!flag2){
            long currentTime = System.currentTimeMillis();
            if(currentTime - startTime > 10000){
                log.error("线程异常终止");
                break;
            }
        }
        log.info("线程运行终止");
    }


    public void testFlag3(){
        new Thread( () -> {
            log.info("修改flag的值为true");
            flag3 = true;
        }).start();
        long startTime = System.currentTimeMillis();
        while(!flag3){
            long currentTime = System.currentTimeMillis();
            if(currentTime - startTime > 10000){
                log.error("线程异常终止");
                break;
            }
        }
        log.info("线程运行终止");
    }
}
