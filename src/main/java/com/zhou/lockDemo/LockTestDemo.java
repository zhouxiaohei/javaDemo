package com.zhou.lockDemo;

import com.zhou.lockDemo.test.ReentrantReadWriteLock2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName LockTestDemo
 * @Author JackZhou
 * @Date 2020/5/22  10:34
 **/
public class LockTestDemo {
    Lock lock = new ReentrantReadWriteLock2(true).readLock();

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            System.out.println(Thread.currentThread() + "----测试进行中----");
        } finally {
            lock.unlock();
        }
    }

    public void test1(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        LockTestDemo demo = new LockTestDemo();
        for(int i =0; i<10; i++){
            CompletableFuture.runAsync( ()-> {
                try {
                    demo.put("124");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, executorService);
        }
    }


    public static void main(String[] args) {
        Node node = new Node();
        node.a = 10;
        node.b = "张三";
        node.test = "test";
        String aa = node.test;
        System.out.println(aa);
        node.test = "test2";
        System.out.println(aa);
        System.out.println( node.test);

    }

  static final class Node{
        int a;
        String b;
      private transient volatile String test;
    }
}


