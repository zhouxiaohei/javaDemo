package com.zhou.threadDemo.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ScheduledThreadPoolDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/7  16:51
 **/
@Slf4j
public class ScheduledThreadPoolDemo implements Runnable{

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //两种创建方式，都是一个代码
        //ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        //使用ScheduledThreadPool来延迟执行Callable或者Runnable，返回一个对象可用来取消或者查询任务执行情况
        //ScheduledFuture future = scheduledExecutorService.schedule(Callable<V> callable, long delay, TimeUnit unit);
//        Callable c1 = new CallableDemo("A"); RunnableDemo r1 = new RunnableDemo();
//        ScheduledFuture future = scheduledExecutorService.schedule(c1, 3, TimeUnit.SECONDS);
//        future.isDone();

         //循环执行任务，在等待initialDelay.unit后执行，然后每隔period.unit后实行;同样返回ScheduledFuture对象；
        //如果此任务的任何一个执行要花费比其周期更长的时间，则将推迟(一定时间)后续执行，但不会同时执行。周期为4秒，执行耗时3秒，每次开始打印间隔一秒;
        //如果任务的任何一个执行遇到异常，则后续执行都会被取消。否则，只能通过执行程序的取消或终止方法来终止该任务。
        //scheduledExecutorService.scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);
        ScheduledThreadPoolDemo scheduledThreadPoolDemo = new ScheduledThreadPoolDemo();
        //scheduledExecutorService.scheduleAtFixedRate(scheduledThreadPoolDemo, 5, 4, TimeUnit.SECONDS);

        //和上一个方法差不多，但是会等上一个任务结束，然后再等待delay时间去执行
        //scheduledExecutorService.scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
        scheduledExecutorService.scheduleWithFixedDelay(scheduledThreadPoolDemo, 5, 4, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        log.info("----执行方法开始---");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("----执行方法结束---");
    }

    public void testSingleScheduled(){
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
//        scheduled.scheduleAtFixedRate(() ->{
//            test();
//        }, 10, 5, TimeUnit.SECONDS);

        scheduled.schedule(() ->{
            test();
        }, 1, TimeUnit.SECONDS);
    }

    public static void test(){
        try {
            log.info("开始时间:{}" + new Date());
            TimeUnit.SECONDS.sleep(3);
            log.info("结束时间:{}" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
