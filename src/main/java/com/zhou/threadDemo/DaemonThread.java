package com.zhou.threadDemo;

/**
 * @ClassName DaemonThread
 * @Author JackZhou
 * @Date 2021/1/26  14:26
 *   Java中有两种线程，一种是用户线程，一种是守护线程，守护线程是一种特殊的线程，就和它的名字一样，它是系统的守护者。
 *
 * 典型的守护线程是垃圾回收线程，当进程中没有非守护线程了，那守护线程就没有必要存在了，用户线程就可以认为是系统的工作线程，它会完成整个系统的业务操作。
 * 用户线程完全结束后就意味着整个系统的业务任务全部结束了，因此系统就没有对象需要守护的了，守护线程自然而然就会退。当一个Java应用，只有守护线程的时候，虚拟机就会自然退出。
 *
 *  这个例子中的线程如果不设置为守护线程，是一个死循环，会一直执行，当我们把它设置为守护线程后，在主线程执行完成后，守护线程也会退出，但是需要注意的是守护线程在退出的时候并不会执行finnaly块中的代码，所以将释放资源等操作不要放在finnaly块中执行，这种操作是不安全的。
 *
 *    线程可以通过setDaemon(true)的方法将线程设置为守护线程。并且需要注意的是设置守护线程要先于start()方法。
 **/
public class DaemonThread extends Thread{
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("i am alive");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("finally block");
            }
        }
    }
    public static void main(String[] args) {
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        //确保main线程结束前能给daemonThread能够分到时间片
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
