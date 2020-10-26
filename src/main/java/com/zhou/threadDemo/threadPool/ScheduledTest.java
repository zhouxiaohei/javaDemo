package com.zhou.threadDemo.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ScheduledTest
 * @Author JackZhou
 * @Date 2020/10/22  11:23
 **/

@Slf4j
public class ScheduledTest {


    static AtomicInteger aa = new AtomicInteger();

    public static void main(String[] args) {
        testOutM();
    }

    //java.lang.OutOfMemoryError: unable to create new native thread
    // 限制内存64M 3200个线程后
    public static void testOutM() {
        log.info("第{}次测试", aa.incrementAndGet());
        for (int i = 0; i < 100; i++) {
            ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
            //延迟多久执行  只执行一次
            scheduled.schedule(() -> {
                test();
            }, 1, TimeUnit.MILLISECONDS);
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testOutM();
    }

    public static void test() {
        try {
            //log.info("开始时间:{}" + new Date());
            TimeUnit.MILLISECONDS.sleep(500);
            //log.info("结束时间:{}" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }
}
