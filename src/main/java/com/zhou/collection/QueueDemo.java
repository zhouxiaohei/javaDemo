package com.zhou.collection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @ClassName QueueDemo
 * @Description
 * @Author JackZhou
 * @Date 2019/7/14  15:52
 **/
public class QueueDemo {

    //remove、element、offer 、poll、peek 是属于Queue接口。
    public static void main(String[] args) {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(100);
        try {
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         add         增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
         remove      移除并返回队列头部的元素          如果队列为空，则抛出一个NoSuchElementException异常
         element     返回队列头部的元素               如果队列为空，则抛出一个NoSuchElementException异常
         offer       添加一个元素并返回true       如果队列已满，则返回false
         poll         移除并返问队列头部的元素    如果队列为空，则返回null
         peek       返回队列头部的元素             如果队列为空，则返回null
         put         添加一个元素                      如果队列满，则阻塞
         take        移除并返回队列头部的元素     如果队列为空，则阻塞
         **/
    }
}
