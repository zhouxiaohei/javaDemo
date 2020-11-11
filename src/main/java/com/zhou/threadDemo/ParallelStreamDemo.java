package com.zhou.threadDemo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ParallelStreamDemo
 * @Author JackZhou
 * @Date 2020/11/10  18:18
 **/

@Slf4j
public class ParallelStreamDemo {

    public static void main(String[] args) throws InterruptedException {
        //testOne();
        //testTwo();
        testThree();
    }


    public static void testOne(){
        int practicalSize = 1000000;

        //ArrayIndexOutOfBoundsException  数组越界
//        List<Integer> integerList = Lists.newArrayList();
//        List<String> strList = Lists.newArrayList();

        List<Integer> integerList = new ArrayList<>(practicalSize);
        List<String> strList = new ArrayList<>(practicalSize/5);

        for (int i = 0; i < practicalSize; i++) {
            strList.add(String.valueOf(i));
        }

        strList.parallelStream().forEach(each -> {
            integerList.add(Integer.parseInt(each));
        });

        log.info("  >>> integerList 预计长度 :: {}", practicalSize);
        log.info("  >>> integerList 实际长度 :: {}", integerList.size());
    }

    //  commonPool的拒绝策略，使用当前线程处理
    public static void testTwo() throws InterruptedException {
        System.out.println(String.format("  >>> 电脑 CPU 并行处理线程数 :: %s, 并行流公共线程池内线程数 :: %s",
                Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.commonPool().getParallelism()));
        List<String> stringList = Lists.newArrayList();
        List<String> stringList2 = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            stringList.add(String.valueOf(i));
        }
        for (int i = 0; i < 4; i++){
            stringList2.add(String.valueOf(i));
        }
        new Thread(() -> {
            System.out.println("初始化线程：" + Thread.currentThread().getName());
            stringList.parallelStream().forEach(each -> {
                System.out.println(Thread.currentThread().getName() + " 开始执行 :: " + each);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }, "子线程-1").start();

        Thread.sleep(1500);

        new Thread(() -> {
            System.out.println("初始化线程：" + Thread.currentThread().getName());
            stringList2.parallelStream().forEach(each -> {
                System.out.println(Thread.currentThread().getName() + " :: " + each);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }, "子线程-2").start();
    }

//  默认ThreadPool的拒绝策略，  抛出RejectedExecutionException异常
    public static void testThree() throws InterruptedException {

        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));

        List<String> stringList = Lists.newArrayList();
        List<String> stringList2 = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            stringList.add(String.valueOf(i));
        }
        for (int i = 0; i < 4; i++){
            stringList2.add(String.valueOf(i));
        }
        new Thread(() -> {
            System.out.println("初始化线程：" + Thread.currentThread().getName());
            stringList.stream().forEach(each -> {
                threadPoolExecutor.execute( () ->{
                    System.out.println(Thread.currentThread().getName() + " 开始执行 :: " + each);
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });
        }, "子线程-1").start();

        Thread.sleep(1500);

        new Thread(() -> {
            System.out.println("初始化线程：" + Thread.currentThread().getName());
            stringList2.stream().forEach(each -> {
                threadPoolExecutor.execute( () ->{
                    System.out.println(Thread.currentThread().getName() + " 开始执行 :: " + each);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });

        }, "子线程-2").start();
    }
}
