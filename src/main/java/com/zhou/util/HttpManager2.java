package com.zhou.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类名称: HttpManager<br>
 * 类描述: http连接池及常用get、post的工具类<br>
 * <p>可以配合spring使用</p>
 * <p>在没有spring的情况下，启动时通过单利包装该类的初始化后使用</p>
 */
public class HttpManager2 {

	private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	private HttpClientBuilder httpClientBuilder;

	public static String DEFAULT_ENCODING= "UTF-8";

	public HttpManager2(HttpClientProperties properties) {
//    	最大连接数
		connectionManager.setMaxTotal(properties.getMaxTotal());
//		设置每个主机地址的并发数
		connectionManager.setDefaultMaxPerRoute(properties.getDefaultMaxPerRoute());

		httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(connectionManager);

		Builder builder = RequestConfig.custom();

//		创建连接的最长时间
		builder.setConnectTimeout(properties.getConnectTimeout());
//		从连接池中获取到连接的最长时间
		builder.setConnectionRequestTimeout(properties.getConnectionRequestTimeout());
//		数据传输的最长时间
		builder.setSocketTimeout(properties.getSocketTimeout());
//		提交请求前测试连接是否可用
		builder.setStaleConnectionCheckEnabled(properties.getStaleConnectionCheckEnabled());
		RequestConfig config = builder.build();
		httpClientBuilder.setDefaultRequestConfig(config);
	}

	/**
	 * 获取httpclient
	 * @return
	 */
	public CloseableHttpClient getHttpClient() {
		return httpClientBuilder.build();
	}

	/**
	 * 发送post请求
	 * @param url 请求地址
	 * @return
	 */
	public String postRequest(String url) {
		return this.postRequest(url, null, null);
	}

	/**
	 * 发送post请求
	 * @param url 请求地址
	 * @param head	请求头
	 * @param params	请求参数
	 * @return 返回String
	 */
	public String postRequest(String url, Map<String, String> head, Map<String, String> params){
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if(head != null){
			for (String key : head.keySet()) {
				String value = head.get(key);
				httpPost.addHeader(new BasicHeader(key, value));
			}
		}

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if(params != null){
			for (String key : params.keySet()) {
				list.add(new BasicNameValuePair(key, params.get(key)));
			}
		}

		String result = null;
		CloseableHttpResponse response = null;
		try{
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Charset.forName(DEFAULT_ENCODING));
			httpPost.setEntity(formEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				result = EntityUtils.toString(entity, Charset.forName(DEFAULT_ENCODING));
			}
		}catch(Exception e){
		}finally{
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 发送get请求
	 * @param url 请求地址
	 * @return
	 */
	public String getRequest(String url){
		return this.getRequest(url, null, null);
	}

	/**
	 * 发送get请求
	 * @param url 请求地址
	 * @param head	请求头
	 * @param params	请求参数
	 * @return 返回String
	 */
	public String getRequest(String url, Map<String, String> head, Map<String, String> params){
		CloseableHttpClient httpClient = getHttpClient();
		HttpGet httpGet = null;

		if(params != null) {
			try {
				URIBuilder uriBuilder = new URIBuilder(url);
				for (String key : params.keySet()) {
					uriBuilder.addParameter(key, params.get(key));
				}
				httpGet = new HttpGet(uriBuilder.build().toString());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}else {
			httpGet = new HttpGet(url);
		}

		if(head != null){
			for (String key : head.keySet()) {
				String value = head.get(key);
				httpGet.addHeader(new BasicHeader(key, value));
			}
		}

		String result = null;
		CloseableHttpResponse response = null;
		try{
			response = httpClient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, Charset.forName(DEFAULT_ENCODING));
			}
		}catch(Exception e){
		}finally{
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
//		HttpClientProperties httpClientProperties = new HttpClientProperties();
//        HttpManager2 httpManager2 = new HttpManager2(httpClientProperties);
//        System.out.println(httpManager2.getRequest("https://blog.csdn.net/Zzhou1990"));
		getNum();
	}


	public static void getNum() {
		//20160910  1609103578
		String key = "160910";
		int tmp = 3577;
		//String url = "http://num.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=11&cityCode=110&monthFeeLimit=0&groupKey=2900302457&net=01&searchCategory=1&advancePayLower=0&searchValue=1222&qryType=02&goodsNet=4&_=1573009329965";
		String url = "http://num.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=11&cityCode=110&monthFeeLimit=0&groupKey=2900302457&net=01&searchCategory=1&advancePayLower=0";
		HttpClientProperties properties = new HttpClientProperties();
		HttpManager httpManager = new HttpManager(properties);

		while (true) {
			++tmp;
			String realKey = key + tmp;
			String realUrl = url + "&searchValue=" + realKey + "&qryType=02&goodsNet=4&_=" + System.currentTimeMillis();
			System.out.println(realUrl);
			String content = httpManager.getRequest(realUrl);
			int numArray = content.indexOf("numArray");
			if (content.substring(numArray + 10, numArray + 12).equals("[]")) {
				continue;
			}
			System.out.println("----" + realKey + "----");
			break;
		}
	}
}
