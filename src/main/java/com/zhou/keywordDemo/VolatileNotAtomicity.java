package com.zhou.keywordDemo;

/**
 * @ClassName VolatileNotAtomicity
 * @Description TODO   volatile并不能保证原子性   https://www.cnblogs.com/yuanqinnan/p/11162682.html
 *   如果让volatile保证原子性，必须符合以下两条规则：
 *
 *         运算结果并不依赖于变量的当前值，或者能够确保只有一个线程修改变量的值；
 *          变量不需要与其他的状态变量共同参与不变约束
 * @Author JackZhou
 * @Date 2021/1/26  15:22
 **/
public class VolatileNotAtomicity {

    private static volatile int counter = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++)
                        counter++;
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter);
    }
}
