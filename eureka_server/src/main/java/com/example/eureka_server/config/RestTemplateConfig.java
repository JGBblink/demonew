package com.example.eureka_server.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * restTemplate配置
 * @author JGB
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(){
		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		// 长连接保持30秒
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
		// 总连接数
		pollingConnectionManager.setMaxTotal(800);
		// 同路由的并发数
		pollingConnectionManager.setDefaultMaxPerRoute(800);
		httpClientBuilder.setConnectionManager(pollingConnectionManager);

		// 保持长连接,需要在头部添加keep-alive
		//List<Header> headers = new ArrayList<>(1);
		//headers.add(new BasicHeader("Connection", "Keep-Alive"));
		//httpClientBuilder.setDefaultHeaders(headers);
		//httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

		HttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		// 连接池获取线程超时时间
		httpRequestFactory.setConnectionRequestTimeout(5000);
		// 服务器建立连接时间
		httpRequestFactory.setConnectTimeout(2000);
		// 等待响应时间
		httpRequestFactory.setReadTimeout(10000);
		// 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
		// clientHttpRequestFactory.setBufferRequestBody(false);

		return new RestTemplate(httpRequestFactory);
	}
}
