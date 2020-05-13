package com.zhou.threadDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @ClassName
 * @Description
 * @Author JackZhou
 * @Date 2019/6/30  18:42
 **/
@Slf4j
public class FutureTaskDemo  {

    public static void main(String[] args) {
        // futureTask 执行Callable
        //futureTaskTestCallRun();
        futureTaskTestCallAsync();
        //futureTaskTestRunnable(); //futureTask 执行Runnable
    }
   // 异步的futureTask被取消
    public static void futureTaskTestCallAsync(){
        CallableDemo callableDemo = new CallableDemo("测试futureTask");
        FutureTask futureTask1 = new FutureTask(callableDemo);
        System.out.println("线程状态：" + futureTask1.isDone());
        Thread thread = new Thread(futureTask1);
        thread.start();//异步
        try {
            System.out.println("cancel结果：" + futureTask1.cancel(true));
            System.out.println("线程状态：" + futureTask1.isDone());
            thread.join();
            System.out.println("执行结果：" + futureTask1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
      * @Author JackZhou
      * @Description  同步执行
     **/
    public static void futureTaskTestCallRun(){
        CallableDemo callableDemo = new CallableDemo("测试futureTask");
        FutureTask futureTask1 = new FutureTask(callableDemo);
        System.out.println("线程状态：" + futureTask1.isDone());
        System.out.println("cancel结果：" + futureTask1.cancel(true));
        futureTask1.run();//同步
        try {
            //System.out.println("cancel结果：" + futureTask1.cancel(true));
            System.out.println("线程状态：" + futureTask1.isDone());
            System.out.println("执行结果：" + futureTask1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void futureTaskTestRunnable(){
        RunnableDemo runnableDemo = new RunnableDemo();
        FutureTask futureTask2 = new FutureTask(runnableDemo, Future.class);
        Thread thread = new Thread(futureTask2);
        thread.start();//异步
        try {
            System.out.println("线程状态：" + futureTask2.isDone());
            System.out.println("执行结果：" + futureTask2.get());
            System.out.println("线程状态：" + futureTask2.isDone());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
