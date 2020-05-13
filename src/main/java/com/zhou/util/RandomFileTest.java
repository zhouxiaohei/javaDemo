package com.zhou.util;

import java.io.RandomAccessFile;

/**
 * @Author JackZhou
 * @Date 2019/9/25  10:20
 **/
public class RandomFileTest {

    /**  这两个构造方法均涉及到一个String类型的参数mode，它决定随机存储文件流的操作模式，其中mode值及对应的含义如下：

     “r”：以只读的方式打开，调用该对象的任何write（写）方法都会导致IOException异常
     “rw”：以读、写方式打开，支持文件的读取或写入。若文件不存在，则创建之。
     “rws”：以读、写方式打开，与“rw”不同的是，还要对文件内容的每次更新都同步更新到潜在的存储设备中去。这里的“s”表示synchronous（同步）的意思
     “rwd”：以读、写方式打开，与“rw”不同的是，还要对文件内容的每次更新都同步更新到潜在的存储设备中去。使用“rwd”模式仅要求将文件的内容更新到存储设备中，而使用“rws”模式除了更新文件的内容，还要更新文件的元数据（metadata），因此至少要求1次低级别的I/O操作
     **/
    /**
     *
     * https://blog.csdn.net/dimudan2015/article/details/81910690
     * https://blog.csdn.net/qq_21808961/article/details/80187662
     *
     * seek(long pos) 将文件记录指针定位到pos位  skip 跳过多少位
     **/
    public static void main(String[] args) throws Exception {
        Employee e1 = new Employee("zhangsan", 23);
        Employee e2 = new Employee("lisi", 24);
        Employee e3 = new Employee("wangwu", 25);
        RandomAccessFile ra = new RandomAccessFile("d:\\employee.txt", "rw");
        ra.write(e1.name.getBytes());
        ra.writeInt(e1.age);
        ra.write(e2.name.getBytes());
        ra.writeInt(e2.age);
        ra.write(e3.name.getBytes());
        ra.writeInt(e3.age);
        ra.close();
        RandomAccessFile raf = new RandomAccessFile("d:\\employee.txt", "r");
        int len = 8;
        raf.skipBytes(12);//跳过第一个员工的信息，其姓名8字节，年龄4字节
        System.out.println("第二个员工信息：");
        String str = "";
        for (int i = 0; i < len; i++) {
            str = str + (char) raf.readByte();
        }
        System.out.println("name:" + str);
        System.out.println("age:" + raf.readInt());
        System.out.println("第一个员工信息：");
        raf.seek(0);//将文件指针移动到文件开始位置
        str = "";
        for (int i = 0; i < len; i++) {
            str = str + (char) raf.readByte();
        }
        System.out.println("name:" + str);
        System.out.println("age:" + raf.readInt());
        System.out.println("第三个员工信息：");
        raf.skipBytes(12);//跳过第二个员工的信息
        str = "";
        for (int i = 0; i < len; i++) {
            str = str + (char) raf.readByte();
        }
        System.out.println("name:" + str);
        System.out.println("age:" + raf.readInt());
        raf.close();
    }
}

class Employee {
    String name;
    int age;
    final static int LEN = 8;

    public Employee(String name, int age) {
        if (name.length() > LEN) {
            name = name.substring(0, 8);
        } else {
            while (name.length() < LEN) {
                name = name + "\u0000";
            }
            this.name = name;
            this.age = age;
        }
    }
}
