package com.zhou.threadDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by 10124 on 2017/6/23.
 */
@Slf4j
public class CallableDemo implements  Callable{

    String callName;

    public CallableDemo(String callName) {
        this.callName = callName;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建两个有返回值的任务
        Callable c1 = new CallableDemo("A");
        Callable c2 = new CallableDemo("B");

        //执行任务并获取Future对象
        Future f1 = pool.submit(c1);
        Future f2 = pool.submit(c2);
        f1.cancel(false);
        f2.cancel(false);
        //从Future对象上获取任务的返回值，并输出到控制台
        try{
            log.info(">>>"+ f1.get().toString());
            log.info(">>>"+ f2.get().toString());
        }catch (Exception e){
            log.info("报错",e);
        }finally {
            System.out.println("休眠");
            TimeUnit.SECONDS.sleep(3);
            System.out.println("休眠结束");
            //关闭线程池
            pool.shutdown();
        }
    }

    @Override
    public Object call() throws Exception {
        TimeUnit.SECONDS.sleep(2);
        log.info("-----执行call方法------");
        return  callName + ":success";
    }
}

