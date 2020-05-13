package com.zhou.lockDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName SynchronizedClerk
 * @Author JackZhou
 * @Date 2020/4/21  10:48
 **/
@Slf4j
public class SynchronizedDemo implements Runnable{

    public Map<Integer, String> map = new ConcurrentHashMap<>();
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void run() {
        while (true) {
            log.info("-------开启后台任务，等待数据到来-----");
            while (map.size()==0) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    log.error("InterruptedException threw by VibrationHistorySevice");
                }
            }
            log.info("开始处理任务");
            long startTime = System.currentTimeMillis();
            int count = 0;
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                map.remove(entry.getKey());
                count++;
            }

            long cost = System.currentTimeMillis() - startTime;
            log.info("此次执行任务数量{}执行耗时{}毫秒", count, cost);
        }

    }
    public void addTask(String task) {
        // 添加成功返回null  存在返回存在的值
        map.put(atomicInteger.decrementAndGet(), task);
        synchronized (this) {
            notify();
        }
    }

    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();
        Thread thread = new Thread(demo);
        thread.start();

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                demo.addTask("任务" + i);
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
