package com.zhou.threadDemo.CompletableFutureDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName CompletableFutureDemoThree
 * @Author JackZhou
 * @Date 2020/5/10  21:45
 **/
@Slf4j
public class CompletableFutureDemoThree {

    public static void main(String[] args) {
        //testAnyOf();
        testAllOf();
    }

    // 任意一个返回，就返回; 多个CompletableFuture
    public static void testAnyOf(){
        CompletableFuture<String> futureTaskA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> futureTaskB = CompletableFuture.supplyAsync(() ->  "world");
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(futureTaskA, futureTaskB);
        try {
            System.out.println(objectCompletableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //多个CompletableFuture 都执行完成才返回
    public static void testAllOf(){
        CompletableFuture<String> futureTaskA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> futureTaskB = CompletableFuture.supplyAsync(() ->  "world");
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futureTaskA, futureTaskB);
        voidCompletableFuture.whenComplete( (t, u) -> System.out.println("任务都完成了 "));
    }

    /**
      * @Author JackZhou
      * @Description  CompletableFuture 出现异常处理策略
     **/
    public static void testFutureR(Boolean flag){
        CompletableFuture.runAsync(() -> {
            log.info("进入CompletableFuture");
            if (flag) {
                throw new RuntimeException("测试异步异常");
            }
            log.info("进入CompletableFuture结束");
        }).whenComplete((r, e) ->{
            if(e != null){
                log.error("执行未完成出现异常", e);
            }
        }).exceptionally(e -> {
            log.error("exceptionally出现异常", e);
            return null;
        }).handle((t, e) -> {
            log.error("hander 处理异常", e);
            return null;
        }).join();
    }
}
