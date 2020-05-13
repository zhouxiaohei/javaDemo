package com.zhou.studyDemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GoodProgram {
	/**
	 * 使用TimeUnit.MINUTES.sleep(2);来替换Thread.currentThread().sleep(4*60*1000);
	 * 精确可读性性更好    
	 * @描述: 同时使用TimeUnit做时间转换
	 * @说明:
	 * @修改时间: 2016年11月1日 下午4:41:53
	 */
	public void exampleTimeSleep(){
		//Thread.currentThread().sleep(4*60*1000);
		
		try {
			TimeUnit.MINUTES.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	TimeUnit.HOURS.toMinutes(2);
	}
	
	/**
	 * 
	 * @描述: 使用yield和temp增加多线程出错的可能性  找出问题
	 * @说明:
	 * @修改时间: 2016年11月1日 下午4:52:57
	 */
	public void exampleConCurrent(){
		Thread.yield();
	}
	
	/**
	 * 实现Callable对象，可以得到线程的返回值  
	 * 
	 * @描述:Lock相比synchronized  可以尝试获取锁 
	 * @说明: 更加自由和方便
	 * @修改时间: 2016年11月1日 下午4:56:50
	 */
	public  void exampleSynchronizedAndLock(){
		Lock lock = new ReentrantLock();
		lock.tryLock();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		CallableExample example = new CallableExample();
		Future<String> future = exec.submit(example);
		//future.get();//阻塞获取任务结果
		future.cancel(true);// 是否终止进行中的任务
		future.isDone();// 是否完成
		future.isCancelled();
	}
	
	class CallableExample implements Callable<String>{
		public String call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * @throws IOException 
	 * @描述: 使用nio替换io 提高效率  多线程防止阻塞自动断开连接
	 * 传统IO每一个对应一个线程。
	 * @说明:
	 * @修改时间: 2016年11月1日 下午6:46:21
	 */
	@SuppressWarnings("resource")
	public void exampleNTO() throws IOException{
		FileChannel channel = new RandomAccessFile("test.txt","rw").getChannel();
		channel.position();//移动通道指向文件的位置
		
		ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
		//wrap.allocate(capacity);
		//wrap.allocateDirect(capacity);
		wrap.put("world".getBytes());
		channel.write(wrap);
		/**
		 * 更快速的解决方案
		 */ 
		ByteBuffer buffer = ByteBuffer.allocate(1024); //指定缓冲器大小
		buffer.flip();//prepare for writing
		channel.write(buffer);
		buffer.clear();// Prepare for Reading
		
	}

}
