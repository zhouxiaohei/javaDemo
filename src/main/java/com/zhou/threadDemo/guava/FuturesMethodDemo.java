package com.zhou.threadDemo.guava;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @ClassName FuturesMethodDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/9  11:24
 **/
@Slf4j
public class FuturesMethodDemo {
    static ExecutorService pool = Executors.newFixedThreadPool(5);//定义线程数

    /**
     * Futures相关
     **/
    //对多个结果进行处理
    //allAsList：对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。注：当其中一个Future失败或者取消的时候，将会进入失败或者取消。
    //successfulAsList：和allAsList相似，唯一差别是对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程。

    //Futures.transform：对于ListenableFuture的返回值进行转换。
    //immediateFuture/immediateCancelledFuture： 立即返回一个待返回值的ListenableFuture。
    //将ListenableFuture 转换成CheckedFuture。CheckedFuture 是一个ListenableFuture ，其中包含了多个版本的get 方法，方法声明抛出检查异常.这样使得创建一个在执行逻辑中可以抛出异常的Future更加容易
    //JdkFutureAdapters.listenInPoolThread(future): guava同时提供了将JDK Future转换为ListenableFuture的接口函数。

    // ThreadFactoryBuilder 方便的threadFactory--- 可以了解下threadFactory
    //ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("测试").build();
    public static void main(String[] args) {
        try {
            List<String> list = createTickets();//获取车票
            List<ListenableFuture<Integer>> futures = Lists.newArrayList();

            ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);
            for (int i = 0; i < list.size(); i++) {
                futures.add(executorService.submit(new Task(list.get(i))));
            }
            testAsList(futures);
            //transforResult(testAsList(futures));
        } finally {
            pool.shutdown();
        }
    }

    public static ListenableFuture<List<Integer>> testAsList(List<ListenableFuture<Integer>> futures) {
        /**   successfulAsList和allAsList  **/
        //allAsList：对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。注：当其中一个Future失败或者取消的时候，将会进入失败或者取消
         ListenableFuture<List<Integer>> resultsFuture = Futures.allAsList(futures);
        //successfulAsList：和allAsList相似，唯一差别是对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程。
         //ListenableFuture<List<Integer>> resultsFuture = Futures.successfulAsList(futures);

        try {
            //所有都执行完毕
            List<Integer> integers = resultsFuture.get();
            log.info("结果:" + integers.stream().map(i -> i + "").collect(Collectors.joining(",")));
        } catch (Exception e) {
            log.error("出错了", e);
        } finally {
            log.info("操作完毕");
        }
        return resultsFuture;
    }

    //将所有的结果,好分割，输出字符串  立即返回一个待返回值的ListenableFuture。
    public static void transforResult(ListenableFuture<List<Integer>> resultsFuture) {
        /** AsyncFunction和  immediateFuture **/
        ListenableFuture<String> transformAsync = Futures.transformAsync(resultsFuture, new AsyncFunction<List<Integer>, String>() {
            @Override
            public ListenableFuture<String> apply(@NullableDecl List<Integer> result) throws Exception {
                return Futures.immediateFuture(result.stream().map(i -> i + "").collect(Collectors.joining(",")));
            }
        }, pool);

        try {
            log.info("转换后的结果{}", transformAsync.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static List<String> createTickets() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("车票" + i);
        }
        return list;
    }

}

@Slf4j
class Task implements Callable<Integer> {
    private String ticket;

    public Task(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public Integer call() throws Exception {
        if (ticket.equals("车票5")) {
            throw new Exception();
        }
        log.info("已卖" + ticket);
        return 1;
    }
}