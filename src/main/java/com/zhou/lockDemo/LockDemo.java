package com.zhou.lockDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockDemo
 * @Author JackZhou
 * @Date 2020/4/21  10:47
 *
 * 因为有时候获得锁的线程发现其某个条件不满足导致不能继续后面的业务逻辑，此时该线程只能先释放锁，等待条件满足。
 * 类似ArrayBlockingQueue  里面还使用了公平锁
 **/

public class LockDemo {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signalAll();
            return x;
        } finally {
            lock.unlock();
        }
    }

    /**   不在本lock的监视器中 报错  **/
//    public synchronized void test() throws InterruptedException {
//        System.out.println(1111);
//        notFull.signal();
//        System.out.println(2222);
//    }
//
//    public static void main(String[] args) {
//        LockDemo demo = new LockDemo();
//        try {
//            demo.test();
//            demo.notFull.signalAll();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
