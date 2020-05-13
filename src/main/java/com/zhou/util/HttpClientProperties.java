package com.zhou.util;

/**
 * 类名称: HttpClientProperties<br>
 * 类描述: http连接池的配置属性类<br>
 * 修改时间: 2016年12月1日上午11:08:51<br>
 */
public class HttpClientProperties {
	
	/**
	 * 最大连接数
	 */
	private int maxTotal = 200;

	/**
	 * 每个路由(route)最大连接数 
	 * 这里route的概念可以理解为 运行环境机器 到 目标机器的一条线路。举例来说，我们使用HttpClient的实现来分别请求 www.baidu.com 的资源和 www.bing.com 的资源那么他就会产生两个route。
	 */
	private int defaultMaxPerRoute = 50;
	
	/**
	 * 创建连接的最长时间
	 */
	private int connectTimeout = 3000;
	
	/**
	 * 从连接池中获取到连接的最长时间 
	 */
	private int connectionRequestTimeout = 10000;
	
	/**
	 * 数据传输的最长时间 
	 */
	private int socketTimeout = 10000;
	
	/**
	 * 提交请求前测试连接是否可用 
	 */
	private boolean staleConnectionCheckEnabled = false;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public boolean getStaleConnectionCheckEnabled() {
		return staleConnectionCheckEnabled;
	}

	public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
		this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
	}
}
