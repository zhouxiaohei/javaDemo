package com.zhou.threadDemo.threadPool;

import com.zhou.threadDemo.CallableDemo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SimpleThreadPool
 * @Description
 * @Author JackZhou
 * @Date 2019/7/2  19:47
 **/
@Slf4j
public class SimpleThreadPool {
    /**  队列：有界队列，无界队列，阻塞队列
       无界队列：  newFixedThreadPool 使用的队列LinkedBlockingQueue
       阻塞队列：  newCachedThreadPool使用的队列SynchronousQueue
       有界队列:  ArrayBlockingQueue
      **/
    public static void main(String[] args) {
        try {
            tesCompletionService();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**  threadPoolExecutor.getActiveCount() 获取的活动线程数不准确，休眠或者切换线程时可能漏计算 **/
    /**
     *  根据阿里对使用线程池的规范：可以参考Executors的实现，按照业务实现自己的线程池。
     **/
    /** 初始化时，来一个任务新建一个一个线程；直到核心线程数满，再往队列里面放任务；如果队列也满了就继续新建线程到最大线程数量；如果最大线程数满就使用拒绝策略，默认拒绝策略是同步执行 **/
    public static void  poolExample(){
        int threadCount = 10;
        // 依次设置核心线程数、最大线程数、空闲存活时间、存活时间单位、队列
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        //固定大小线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //无界线程池
        Executors.newCachedThreadPool();
        //单个线程池  只有一个线程池的固定大小线程池
        Executors.newSingleThreadExecutor();

        /** ---------------- 其他线程池--------------**/
        //用于任务调度的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        //工作窃取线程池
        ExecutorService executorServiceFk = Executors.newWorkStealingPool();
    }

    /**
      * @Description  原理:内部通过阻塞队列+FutureTask，实现了任务先完成可优先获取到，即结果按照完成先后顺序排序。
     **/
   public static void tesCompletionService() throws InterruptedException, ExecutionException {
       ExecutorService executorService = Executors.newFixedThreadPool(10);
       ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(executorService);
       int taskCount = 100;

       for (int i = 0; i < taskCount; i++) {
           executorCompletionService.submit(new CallableDemo("测试线程" + i));
       }
       //任务先完成的先获取到
       for (int i = 0; i < taskCount; i++) {
           log.info("获取执行结果" + executorCompletionService.take().get());
       }
       log.info("-----------执行结束-------------");
   }
}
