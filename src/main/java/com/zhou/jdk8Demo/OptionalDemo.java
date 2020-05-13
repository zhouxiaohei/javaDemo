package com.zhou.jdk8Demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

/**
 * @ClassName OptionalDemo
 * @Author JackZhou
 *
 *   类似integer是int的封装类，Optional是所有对象的封装类
 *
 *  Optional.empty() 创建一个空的Optional实例
 *  Optional.of(T t)  创建一个Optional实例,t必须非空
 *  Optional.ofNullable(T t) 创建一个Optional实例,允许为空
 *
 *  optional.ifPresent() 判断是否包含对象
 *  ifPresent(Consumer<? super T> consumer)  如果有值就执行consumer接口
 *
 *   optional.get()  有值返回，没值抛出异常
 *   optional.orElse(T other)  有值返回，没值返回指定的other对象
 *   orElseGet(Supplier<? extends T> other)  有值返回，没值返回由Supplier接口提供的对象
 *   orElseThrow(Supplier<? extends T> other) 有值返回，没值返回由Supplier接口抛出的异常
 *
 * @Date 2020/4/15  18:00
 **/
public class OptionalDemo {

    public static void main(String[] args) {
        //Optional<Object> optional = Optional.empty();
        Boy boy = null;
        Optional<Boy> optionBoy = Optional.ofNullable(boy);
        Boy otherBoy = optionBoy.orElse(new Boy(new Girl("翠花")));
        // 以此类推
    }
}

@Data
@AllArgsConstructor
class Boy{
    private Girl girl;
}
@Data
@AllArgsConstructor
class Girl{
    private String name;
}