package com.zhou.threadDemo.guava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ListenableFutureDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/10  18:18
 **/
@Slf4j
public class ListenableFutureDemo implements Callable<String>{

    String info;

    public ListenableFutureDemo(String info) {
        this.info = info;
    }

    public static void main(String[] args) {
        log.info("线程{}, 主线程开始---" , Thread.currentThread().getName());
        ExecutorService pool = Executors.newFixedThreadPool(10);
        //将java的包装成Listenable的线程池  Guava提供的返回值是ListenableFuture的线程池
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);
        ListenableFuture<String> future1 = executorService.submit(new ListenableFutureDemo("昨夜西风凋碧树"));
        ListenableFuture<String> future2 = executorService.submit(new ListenableFutureDemo("独上高楼"));
        //ListenableFuture可以设置callBack  addCallback在addListener基础上封装；
        //在线程执行成和执行失败的策略；设置线程池异步执行callback、不然就是同步执行
        Futures.addCallback(future1, new CallBackDemo(), MoreExecutors.directExecutor());
        Futures.addCallback(future2, new CallBackDemo(), pool);
        log.info("主线程结束");
    }

    @Override
    public String call() throws Exception {
        log.info("线程{}, 工作线程执行完成：{}" , Thread.currentThread().getName(), info);
//        if(true){
//            throw new RuntimeException("我执行失败了");
//        }
        return info;
    }
}

@Slf4j
class CallBackDemo implements FutureCallback<String> {

    @Override
    public void onSuccess(@NullableDecl String result) {
        log.info("线程{}, callback成功响应开始：{}" , Thread.currentThread().getName(), result);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("线程{}, callback成功响应结束：{}" , Thread.currentThread().getName(), result);
    }

    @Override
    public void onFailure(Throwable throwable) {
        log.error("线程{}, callback失败响应：{}" , Thread.currentThread().getName());
    }
}