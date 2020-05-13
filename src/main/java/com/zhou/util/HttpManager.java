package com.zhou.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 类名称: HttpManager<br>
 * 类描述: http连接池及常用get、post的工具类<br>
 * <p>可以配合spring使用</p>
 * <p>在没有spring的情况下，启动时通过单利包装该类的初始化后使用</p>
 * 修改时间: 2016年12月1日上午11:09:07<br>
 */
public class HttpManager {

    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    private HttpClientBuilder httpClientBuilder;

    public static String DEFAULT_ENCODING= "UTF-8";

	private static String blankEncode = null;

	static{
		try {
			blankEncode = URLEncoder.encode(" ",DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public HttpManager(HttpClientProperties properties) {
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
	 * 发送get请求
	 * @param url 请求地址
	 * @return
	 * TODO
	 */
	public String getRequest(String url){
		return getRequest(url, null);
	}

	/**
	 * 通用爬虫改造
	 * 发送get请求
	 * @return 返回String
	 */
	public String getRequest(String url, String encoding){
		CloseableHttpClient httpClient = getHttpClient();
		String result = null;
		CloseableHttpResponse response = null;
		try{
			HttpGet httpGet = new HttpGet(url.replace(" ", blankEncode));
			//伪装浏览器头
			httpGet.addHeader(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36"));
			response = httpClient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, Charset.forName(encoding == null ? DEFAULT_ENCODING : encoding));
			}
		}catch(Exception ex){
			System.out.println("执行url出错:" + url + "--:" + ex.getMessage());
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
	 * @描述: 用于文件下载
	 * @说明:
	 * @修改时间: 2017年3月14日 下午5:27:59
	 * @param url
	 * @param filePath
	 */
	public boolean download(String url, String filePath) {
		boolean flag = false;
		CloseableHttpClient httpClient = getHttpClient();
		InputStream is = null;
		OutputStream os = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url.replace(" ", blankEncode));
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				os = new FileOutputStream(filePath);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				os.flush();
				flag = true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static void main(String[] args) {
//		HttpClientProperties properties = new HttpClientProperties();
//		HttpManager httpManager = new HttpManager(properties);
//
//		String content = httpManager.getRequest("http://num.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=11&cityCode=110&monthFeeLimit=0&groupKey=2900302457&net=01&searchCategory=1&advancePayLower=0&searchValue=12&qryType=02&goodsNet=4&_=1573009329965");
//		System.out.println("---开始---");
//		System.out.println(content);
//		System.out.println("---完成---");
//		HtmlparserTemplate htmlparserTemplate = new HtmlparserTemplate(content, "gb2312");
//		System.out.println(htmlparserTemplate.getEncoding());

		//test download
//		httpManager.download("http://www.srgs.net//srgs/about/", "C:\\Users\\sks\\Desktop\\abc\\about");
//		httpManager.download("http://www.srgs.net//templets/qyskin/images/test/wdfzzsr.jpg", "C:\\Users\\sks\\Desktop\\abc\\wdfzzsr.jpg");
//		httpManager.download("http://www.srgs.net//srgs/yuganxiangetisiyingjingjixiehui/changwulishi/2016/1010/1867.html", "C:\\Users\\sks\\Desktop\\abc\\1867.html");
//		httpManager.download("http://www.srgs.net//templets/qyskin/css/css.css", "C:\\Users\\sks\\Desktop\\abc\\css.css");
		//getNum();
	   System.out.println(System.currentTimeMillis() );
	}


	public static void getNum(){
		String key = "1222";
		int tmp = 21062;
		//2221  3008
		//String url = "http://num.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=11&cityCode=110&monthFeeLimit=0&groupKey=2900302457&net=01&searchCategory=1&advancePayLower=0&searchValue=1222&qryType=02&goodsNet=4&_=1573009329965";
		String url = "http://num.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=11&cityCode=110&monthFeeLimit=0&groupKey=2900302457&net=01&searchCategory=1&advancePayLower=0";
		HttpClientProperties properties = new HttpClientProperties();
		HttpManager httpManager = new HttpManager(properties);

		while (true){
			++tmp;
			String realKey = key + tmp;
			String realUrl =  url + "&searchValue=" + realKey  + "&qryType=02&goodsNet=4&_=" + System.currentTimeMillis();
			System.out.println(realUrl);
			String content = httpManager.getRequest(realUrl);
			int numArray = content.indexOf("numArray");
			if(content.substring(numArray+10, numArray + 12).equals("[]")){
				continue;
			}
			System.out.println("----"+ realKey + "----");
			break;
		}

	}
}
