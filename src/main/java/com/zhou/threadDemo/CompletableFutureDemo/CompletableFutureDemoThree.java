package com.zhou.threadDemo.CompletableFutureDemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName CompletableFutureDemoThree
 * @Author JackZhou
 * @Date 2020/5/10  21:45
 **/
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
}
