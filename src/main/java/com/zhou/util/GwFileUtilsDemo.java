package com.zhou.util;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * @Author JackZhou
 * @Date 2019/9/23  15:02
 **/
@Slf4j
public class GwFileUtilsDemo {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String path = "C:\\Users\\T470\\Desktop\\event.csv";
//        readFileByLine(path);
//        log.info(System.getProperty("user.dir"));
        readFileTest(path);
    }

        /**
          * @Author JackZhou
          * @Description  一次性读写短文件
         **/
        public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * FileInputStream 可以根据文章长度一次性读取所有内容
     *              对短文件或者需要整体加载的文件方便且效率高
     *
     *      多次读取，当执行到文件结尾时,返回 -1
     * **/
    public static void readFileTest(String path){
        try {
            File file = new File(path);
            Long length = file.length();
            FileInputStream fis = new FileInputStream(path);
           // byte[] content = new byte[length.intValue()];
            byte[] content =  new byte[1024];
            int currentLen;
            while((currentLen = fis.read(content)) != -1) {
                String str = new String(content, 0, currentLen);
                //log.info(str);
                printMem();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 使用java.util.Scanner类
     *
     *  方法②
     * Apache Commons IO流
     * LineIterator it = FileUtils.lineIterator(theFile, UTF-8);
     * try {
     *  while (it.hasNext()) {
     *  String line = it.nextLine();
     *  // do something with line
     *   }
     * } finally {
     *  LineIterator.closeQuietly(it);
     * }
     * **/
    public static void readFileByLine(String path) {
        FileInputStream inputStream = null;
        Scanner sc = null;
        int count = 0;
        try {
            inputStream = new FileInputStream(path);
//            Charset.forName("UTF-8");
//            Charset.forName("GBK");
            sc = new Scanner(inputStream, "utf-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                count++;
                if(count % 100 == 0){
                    log.info("第{}条数据，内存情况如下", count);
                    printMem();
                }
            }
        } catch (IOException e) {
            log.error("Io异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }


    /**
     * 按字节截取字符串 ，指定截取起始字节位置与截取字节长度
     *
     * @param orignal
     *            要截取的字符串
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException
     *             使用了JAVA不支持的编码格式
     */
    public static String substringByte(String orignal, int start, int count) {

        // 如果目标字符串为空，则直接返回，不进入截取逻辑；
        if (orignal == null || "".equals(orignal))
            return orignal;

        // 截取Byte长度必须>0
        if (count <= 0)
            return orignal;

        // 截取的起始字节数必须比
        if (start < 0)
            start = 0;

        // 目标char Pull buff缓存区间；
        StringBuffer buff = new StringBuffer();

        try {
            // 截取字节起始字节位置大于目标String的Byte的length则返回空值
            if (start >= getStringByteLenths(orignal))
                return null;
            int len = 0;
            char c;
            // 遍历String的每一个Char字符，计算当前总长度
            // 如果到当前Char的的字节长度大于要截取的字符总长度，则跳出循环返回截取的字符串。
            for (int i = 0; i < orignal.toCharArray().length; i++) {
                c = orignal.charAt(i);

                // 当起始位置为0时候
                if (start == 0) {
                    len += String.valueOf(c).getBytes("GBK").length;
                    if (len <= count)
                        buff.append(c);
                    else
                        break;
                } else {
                    // 截取字符串从非0位置开始
                    len += String.valueOf(c).getBytes("GBK").length;
                    if (len >= start && len <= start + count) {
                        buff.append(c);
                    }
                    if (len > start + count)
                        break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 返回最终截取的字符结果;
        // 创建String对象，传入目标char Buff对象
        return new String(buff);
    }

    /**
     * 计算当前String字符串所占的总Byte长度
     *
     * @param args
     *            要截取的字符串
     * @return 返回值int型，字符串所占的字节长度，如果args为空或者“”则返回0
     * @throws UnsupportedEncodingException
     */
    public static int getStringByteLenths(String args)
            throws UnsupportedEncodingException {
        return args != null && args != "" ? args.getBytes("GBK").length : 0;
    }

    /**
       totalMemory 虚拟机已经从操作系统拿到的内存
       maxMem 虚拟机可以从操作系统拿到的最大内存
       freeMem  totalMem - 已经使用的内存
     **/
    public static void printMem(){
        log.info("totalMemory:{}, maxMemory:{}, freeMemory:{}", byteToM(Runtime.getRuntime().totalMemory()), byteToM(Runtime.getRuntime().maxMemory()), byteToM(Runtime.getRuntime().freeMemory()));
    }

    /**
     * 把byte转换成M
     * @param bytes
     * @return
     */
    public static long byteToM(long bytes){
        long m =  (bytes / 1024 / 1024);
        return m;
    }
}
