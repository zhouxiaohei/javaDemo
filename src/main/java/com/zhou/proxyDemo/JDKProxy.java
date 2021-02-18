package com.zhou.proxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName JDKProxy
 * @Author JackZhou
 * @Date 2021/2/18  12:10
 **/
public class JDKProxy implements InvocationHandler {

    //目标对象引用
    private Object target;

    //构造器，可以动态传入目标对象
    public JDKProxy(Object target) {
        this.target = target;
    }

    //创建代理对象
    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    //利用反射机制调用目标对象的方法，并增强行为
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        //使用反射机制
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    private void after() {
        System.out.println("JDK已经完成代理");
    }

    private void before() {
        System.out.println("JDK准备开始代理");
    }

    public static void main(String[] args) {
        //目标对象
        GoalObject goalObject = new GoalObject();
        //JDK代理对象,使用构造函数传入对象参数
        JDKProxy jDKProxy = new JDKProxy(goalObject);
        //JDK代理反射机制，获取对象信息（对象是接口）
        ProxyInterface proxyInterface = (ProxyInterface) jDKProxy.getProxy();
        //通过代理获取的对象，该对象调用其增强行为
        proxyInterface.proxyInterface();
    }
}
