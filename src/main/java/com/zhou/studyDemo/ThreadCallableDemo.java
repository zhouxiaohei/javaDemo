package com.zhou.studyDemo;

import java.util.concurrent.*;

/**
 * Created by 10124 on 2017/6/23.
 */
public class ThreadCallableDemo  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建两个有返回值的任务
        Callable c1 = new ThreadDemo("A");
        Callable c2 = new ThreadDemo("B");
        //执行任务并获取Future对象
        Future f1 = pool.submit(c1);
        Future f2 = pool.submit(c2);
        //从Future对象上获取任务的返回值，并输出到控制台
        System.out.println(">>>"+ f1.get().toString());
        System.out.println(">>>"+ f2.get().toString());
        //关闭线程池
        pool.shutdown();
    }
}

class ThreadDemo implements Callable{
    String callName;

    public ThreadDemo(String callName) {
        this.callName = callName;
    }

    @Override
    public String call() throws Exception {
       return  callName + ":success";
    }
}