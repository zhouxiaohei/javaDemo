package com.zhou.keywordDemo;

/**
 * @ClassName VolatileTest
 * @Author JackZhou
 * @Date 2021/1/26  15:18
 **/
public class VolatileTest {
    private static boolean isOver = false;
    private static int a = 1;

    // 这里的代码会出现死循环，原因在于虽然在主线程中改变了isOver的值
    //TODO 错误，JVM应该已经优化。此处死循环有概率，但是比较小
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isOver) {
                    a++;
                }

            }
        });
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isOver = true;
    }
}
