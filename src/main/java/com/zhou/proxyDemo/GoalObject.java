package com.zhou.proxyDemo;

/**
 * @ClassName GoalObject
 * @Author JackZhou
 * @Date 2021/2/18  12:10
 **/
public class GoalObject implements ProxyInterface {
    //实现接口
    @Override
    public void proxyInterface() {
        System.out.println("实现目标对象功能");
    }
}
