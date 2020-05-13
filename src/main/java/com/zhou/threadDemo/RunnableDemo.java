package com.zhou.threadDemo;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName RunnableDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/6/30  16:42
 **/
@Slf4j
public class RunnableDemo implements Runnable{

    @Override
    public void run() {
        log.info("-------10秒后大军来袭-----");
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        synchronized (this){
            try {
                this.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("-------keep going-----");
    }

    public static void main(String[] args) {

        Thread thread = new Thread(new RunnableDemo());
        thread.start();
        //thread.interrupt(); //报错InterruptedException 但是不结束
        thread.yield(); //释放当前cpu的执行权、回到就绪状态.
//        try {
//            thread.join(); //执行当前线程方法指导执行结束
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        log.info("----执行结束----");
    }
}
