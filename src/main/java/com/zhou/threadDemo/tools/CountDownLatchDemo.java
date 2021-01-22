package com.zhou.threadDemo.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CountDownLatchDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/22  17:47
 **/

@Slf4j
public class CountDownLatchDemo {

    /** 多线程控制类，计数器栅栏，当计数器满足条件才往下执行 ***/
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(new taskA(countDownLatch));
        executor.execute(new taskB(countDownLatch));

        //等待事情都办完
        countDownLatch.await();
        log.info("----小两口可以一起吃饭了-----");
    }
}

@Slf4j
class taskA implements  Runnable{

    private CountDownLatch countDownLatch;

    public taskA(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            log.info("丈夫去炒菜");
            TimeUnit.SECONDS.sleep(3);
            countDownLatch.countDown();
            log.info("丈夫炒菜完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Slf4j
class taskB implements  Runnable{

    private CountDownLatch countDownLatch;

    public taskB(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            log.info("媳妇去盛饭");
            TimeUnit.SECONDS.sleep(3);
            countDownLatch.countDown();
            log.info("媳妇盛饭完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
