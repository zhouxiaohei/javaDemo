package com.zhou.threadDemo.guava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName GuavaDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/10  20:25
 **/
@Slf4j
public class SettableFutureDemo implements Callable<String> {

    static ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public static void main(String[] args) {
        testSettableFuture(); //规定时间拿到结果；适合需要在规定时间拿结果的应用;指定时间拿不到结果抛出异常
    }

    //如果执行任务线程超时，并不会中断执行;set完成后即可拿到响应结果
    public static void testSettableFuture(){
        SettableFuture<String> settableFuture = SettableFuture.create();
        ListenableFuture<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                settableFuture.set("得到世界");
                log.info("会继续走下去么??");
                return "测试";
            }
        });
        try {
            //future.cancel(true);
            Futures.addCallback(future,  new FutureCallback<String>() {
                @Override
                public void onSuccess(@Nullable String result) {
                    log.info("执行成功响应：{}", result);
                }

                @Override
                public void onFailure(Throwable th) {
                    log.error("执行失败", th);
                }
            } ,executorService);
            log.info("settableFuture 规定时间检测：" + settableFuture.get(2, TimeUnit.SECONDS));
            // set完成后即可拿到响应结果
            //log.info("settableFuture 规定时间检测：" + settableFuture.get(10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep(2);
        log.info("任务执行");
        return "测试";
    }
}
