package com.zhou.mathOperation;

/**
 * @ClassName mathOperation
 * @Author JackZhou
 * @Date 2020/8/12  17:22
 **/
public class SimpleOperation {

    public static void main(String[] args) {
        //transformTest();
        //simpleTest();
        //simpleTestApp();
        simpleExample();
    }

    public static void transformTest(){
        //十进制转二进制
        System.out.println(Integer.toBinaryString(10)); //1010
        System.out.println(Integer.toBinaryString(-10)); //11111111111111111111111111110110
        //二进制转十进制
        System.out.println(Integer.parseInt("1010", 2));
        System.out.println(Integer.parseInt("01111111111111111111111111111011", 2)); //2147483643
    }

    /**
      * @Author JackZhou
      * @Description
     *     左移( << )、右移( >> )、无符号右移( >>> )
     *     左移，转换成二进制左移N位（相当于N个2；右移动同理），正数用0补位 最小值为0， 负数用1补位最大值为-1。 无符合右移用0补位。
     *     &  |  ^ 都是转成2进制，与(相乘)、或(有一个为1就是1)、^(0和1的1，不然得0)
     **/
    public static void simpleTest(){
        int aa = 10; // 1010
        // 左移  转换成二进制左移N位 左移一位乘2
        //  10 << 2 = 10 * 2 * 2 =40
        System.out.println(aa << 2);
        //右移转换成二进制右移N位  右移一位除以2
        System.out.println(10 >> 2);
        // >>> 无符号右移，忽略符号位，空位都以0补齐
        System.out.println(Integer.MAX_VALUE); // 2147483647
        System.out.println( (-10) >>> 1);  // 2147483643  11111111111111111111111111110110 右移一位   01111111111111111111111111111011
        // &操作  与操作  转换为2进制, 1和1得1  其他得0
        System.out.println(10 & 11);  //1010 & 1011  = 1010 =10
        // | 操作  或操作  有一个1得1
        System.out.println(10 | 11);  //1010 | 1011  = 1011 =11
        // ^ 操作  取反 非操作  0和1得1  不然得0
        System.out.println( 10 ^ 11);  //1010 ^ 1011  = 0001 = 1
    }

    /**
      * @Author JackZhou
      * @Description  &= 按位与赋值
     *
     * |=  按位或赋值
     *
     * ^= 按位异赋值
     *
     * >>= 右移赋值
     *
     * >>>= 无符号右移赋值
     *
     * <<= 赋值左移
     *
     *  ++n 操作之前加1,n++ 操作之后加1
     *
     * 和 += 一个概念而已。
     **/
    public static void simpleTestApp(){
        //  int a = 11 | 10;
        int a = 11;
        a |= 10;
        System.out.println(a);

        a ^= 10;
        System.out.println(a);

        a &= 10;
        System.out.println(a);

    }

    /**
      * @Author JackZhou
      * @Description    用于整数的奇偶性判断   一个整数a, a & 1  1代表奇数 0代表偶数
     **/
    public static void simpleExample(){
        // 奇偶校验
        int a = 2,b = 3;
        System.out.println((a & 1) + "---分割线----" + (b & 1));

        // hashMap  扰乱函数  移动到高低位
        //https://www.zhihu.com/question/20733617 右位移16位，正好是32bit的一半，自己的高半区和低半区做异或，就是为了混合原始哈希码的高位和低位，以此来加大低位的随机性
        //hashCode是int类型散列值 2的32次方正负数  而hashmap初始化为16
        String aa = "test";
        int hashCode = aa.hashCode();
        int hashCodeFix = hashCode ^ (hashCode >>> 16);
        System.out.println( hashCode + "扰乱后" +  hashCodeFix);
        // 再用扰乱后的 hashCode和 长度
        int index = hashCodeFix & (16 - 1);
        System.out.println("得到hash的数组下标" + index);

        //判断是否是2的正整数幂等  2的n次方
        int aaa = 16, bbb = 15; // 16 = 10000 15= 1111
        System.out.println((aaa & (aaa-1)) + "---分割线----" + (bbb & (bbb-1)));

    }

}
