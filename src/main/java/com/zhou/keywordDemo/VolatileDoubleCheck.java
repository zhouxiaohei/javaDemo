package com.zhou.keywordDemo;

/**
 * @ClassName VolatileDoubleCheck
 * @Author JackZhou
 * @Date 2021/1/26  15:20
 **/
public class VolatileDoubleCheck {

    private volatile static VolatileDoubleCheck instance;

    private VolatileDoubleCheck() {
    }

    public VolatileDoubleCheck getInstance() {
        if (instance == null) {//步骤1
            synchronized (VolatileDoubleCheck.class) {//步骤2
                if (instance == null) {//步骤3
                    instance = new VolatileDoubleCheck();//步骤4
                }
            }
        }
        return instance;
    }

}
