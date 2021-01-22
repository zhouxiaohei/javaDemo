package com.zhou.threadDemo.tools;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SemaphoreTest
 * @Author JackZhou
 * @Date 2020/8/19  18:28
 **/
public class SemaphoreTest {

    public static void main(String[] args) {
        test1();
        //test2();
    }

    public static void test1() {
        Semaphore semaphore = new Semaphore(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(100));

        for (int i = 0; i < 10; i++) {
            int a = i;
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release(1);
            CompletableFuture.runAsync(() -> {

                try {
                    semaphore.acquire(1);
                    System.out.println(new Date() + "开始执行" + a);
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {

                } finally {
                    semaphore.release(1);
                }

                System.out.println(new Date() + "结束执行" + a);
            }, threadPoolExecutor);
            System.out.println(new Date() + "加入执行队列：" + a);
        }
    }

    public static void test2() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(100));
        for (int i = 0; i < 10; i++) {
            int a = i;
            while (threadPoolExecutor.getQueue().size() > 0) {
                try {
                    System.out.println("进来休息一下");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CompletableFuture.runAsync(() -> {
                execTest(a);
            }, threadPoolExecutor);
            System.out.println(new Date() + "加入执行队列：" + i);

            if(i == 9){
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadPoolExecutor.getQueue().size());
            }

        }
    }

    public static void execTest(int a) {
        try {
            System.out.println(new Date() + "开始执行" + a);
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {

        }
        System.out.println(new Date() + "结束执行" + a);
    }

}
