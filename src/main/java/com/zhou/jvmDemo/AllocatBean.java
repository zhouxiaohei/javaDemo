package com.zhou.jvmDemo;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AllocatBean
 * @Author JackZhou
 * @Desc  对象分配到老年代日志
 * @Date 2021/2/7  9:50
 **/
public class AllocatBean {

    // 1024=1024*1000
    // 参数：-Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails
    // -XX:PretenureSizeThreshold=1024000
    // -Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1024000
    public static void main(String[] args) {
        Map<Integer, byte[]> m = new HashMap<Integer, byte[]>();
        for (int i = 0; i < 5; i++) {
            byte[] b = new byte[1024 * 1024];
            m.put(i, b);
        }
    }
}
