package com.zhou.crawl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @Author JackZhou
 * @Date 2019/10/15  11:41
 **/
@Slf4j
public class IpProxyDemo {
    public static void main(String[] args) {
        simpleDemo();
    }

    public static void simpleDemo() {
        //HttpHost proxy = new HttpHost("103.78.73.92", 3128, "https");
        HttpHost proxy = new HttpHost("119.15.91.137",50712,"https");
        //把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpGet = new HttpGet("https://blog.csdn.net/Zzhou1990/article/details/68486862");

        CloseableHttpResponse httpResp = null;
        try {
            httpResp = httpclient.execute(httpGet);
            int statusCode = httpResp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("成功");
            }
        } catch (Exception e) {
            log.info("执行请求报错:", e);
        } finally {
            try {
                if(httpResp != null){
                    httpResp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
