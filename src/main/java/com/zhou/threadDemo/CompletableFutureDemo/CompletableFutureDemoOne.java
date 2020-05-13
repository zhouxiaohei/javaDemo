package com.zhou.threadDemo.CompletableFutureDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CompletableFutureDemo  talk is cheap，show me the code；
 * @Description jdk8提供对future的升级，可能会优于guava的ListenableFuture
 *            CompletableFuture 比 ListenableFuture更灵活
 * @Author JackZhou
 * @Date 2019/7/16  11:42
 **/
@Slf4j
public class CompletableFutureDemoOne {

    /**
     * Async结尾的方法都是可以异步执行的，如果指定了线程池，会在指定的线程池中执行，如果没有指定，默认会在ForkJoinPool.commonPool()中执行。
     **/
    public static void main(String[] args) {
        //testSimpleUsage();
        testTransformResult();
    }

    /**
     *  run/supply  无返回值/有返回值
     *  public static   CompletableFuture<Void>     runAsync(Runnable runnable)
     public static   CompletableFuture<Void>     runAsync(Runnable runnable, Executor executor)
     public static <U> CompletableFuture<U>  supplyAsync(Supplier<U> supplier)
     Supplier 是什么

     public CompletableFuture<Void> thenRun(Runnable action)
     public CompletableFuture<Void> thenRunAsync(Runnable action)
     public CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor)

     future.whenComplete
     **/
    public static void testSimpleUsage() {
        log.info("线程{}, 开始简单应用测试", Thread.currentThread().getName());
        // case1: supplyAsync
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("线程{},执行有返回值任务", Thread.currentThread().getName());
            return "返回结果:成功";
        });

        // thenRun，与supplyAsync同线程
        future.thenRun(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程{},上一个任务执行完成，执行下一个任务①完成", Thread.currentThread().getName());
        });

        //thenRunAsync，另启动线程执行
        future.thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程{},上一个任务执行完成，执行下一个任务②完成", Thread.currentThread().getName());
        });

        // 主动触发Complete结束方法
        // future.complete("Manual complete value.");
        future.whenComplete((v, e) -> {
            log.info("线程{}，执行结果: {}",  Thread.currentThread().getName(), v);
            log.info("线程{}, 执行异常信息: {}", Thread.currentThread().getName(), e);
        });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("线程{} ,简单应用测试结束了", Thread.currentThread().getName());
    }

    /**
     *   变换结果 这些方法的输入是上一个阶段计算后的结果，返回值是经过转化后结果
     *  public <U> CompletionStage<U> thenApply(Function<? super T,? extends U> fn);
        public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn);
        public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn,Executor executor);

         聚合结果
         public <U,V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
         public <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
         public <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn, Executor executor)

        传递结果 同步异步
        thenCompose()：第一个CompletableFuture执行完毕后，传递给下一个CompletionStage作为入参进行操作。
        thenComposeAsync()
     **/
    public static void testTransformResult() {
        //转换结果
        String result = CompletableFuture.supplyAsync(() ->  "Hello ").thenApplyAsync(v -> v + "world").join();
        log.info("异步转换结果是 {}", result);

        //聚合结果
        CompletableFuture<String> futureTaskA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> futureTaskB = CompletableFuture.supplyAsync(() ->  "world");
        CompletableFuture<String> combineResult = futureTaskA.thenCombine(futureTaskB, (x, y) -> x + "-" + y);
        try {
            log.info("合并结果为{}", combineResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //传递结果 结果由上一个CompletableFuture的结果组成
        CompletableFuture<String> futureTaskAA = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> completableFuture = futureTaskAA.thenCompose(string -> CompletableFuture.supplyAsync(() -> string + " world"));
        try {
            log.info("结果传递最终值为:{}", completableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
