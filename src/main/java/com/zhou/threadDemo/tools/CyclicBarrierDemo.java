package com.zhou.threadDemo.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName CyclicBarrierDemo
 * @Author JackZhou
 * @Date 2021/1/22  17:15
 **/
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        /*
        田径比赛
         */
        //当给定数量的线程（线程）等待时，它将跳闸，当屏障跳闸时执行给定的屏障动作，由最后一个进入屏障的线程执行。
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("所有参赛选手已准备就绪，比赛开始！");
        });
        for (int i = 1; i <= 7; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "号选手准备就绪！");
                try {
                    cyclicBarrier.await();//等待所有 parties已经在这个障碍上调用了 await 。（让线程阻塞直到有7个线程阻塞于此）
                    System.out.println(Thread.currentThread().getName() + "号选手冲入终点！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }

    }
}
