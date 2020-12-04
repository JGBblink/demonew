package com.example.eureka_client.rest.es;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author: JGB
 */
@RestController
@RequestMapping("/es")
public class ElasticsearchRest {

	@Autowired
	RestHighLevelClient restHighLevelClient;

	/**
	 * 创建索引
	 * @throws IOException
	 */
	@PostMapping("/index")
	public void creatIndex() throws IOException {
		//创建索引请求对象
		CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
		//设置索引参数
		createIndexRequest.settings(Settings.builder().put("number_of_shards",1).put("number_of_replicas",0));
		//设置映射
		createIndexRequest.mapping("{\n" +
				"\"properties\": {\n" +
				"\"name\": {\n" +
				"\"type\": \"text\",\n" +
				"\"analyzer\":\"ik_max_word\",\n" +
				"\"search_analyzer\":\"ik_smart\"\n" +
				"},\n" +
				"\"description\": {\n" +
				"\"type\": \"text\",\n" +
				"\"analyzer\":\"ik_max_word\",\n" +
				"\"search_analyzer\":\"ik_smart\"\n" +
				"},\n" +
				"\"studymodel\": {\n" +
				"\"type\": \"keyword\"\n" +
				"},\n" +
				"\"price\": {\n" +
				"\"type\": \"float\"\n" +
				"},\n" +
				"\"timestamp\": {\n" +
				"\"type\": \"date\",\n" +
				"\"format\": \"yyyy‐MM‐dd HH:mm:ss||yyyy‐MM‐dd||epoch_millis\"\n" +
				"}\n" +
				"}\n" +
				"}\n" ,XContentType.JSON);
		//链接客户端
		IndicesClient client = restHighLevelClient.indices();
		//创建响应对象
		CreateIndexResponse createIndexResponse = client.create(createIndexRequest, RequestOptions.DEFAULT);
		//响应
		boolean acknowledged = createIndexResponse.isAcknowledged();
		System.out.println(acknowledged);
	}

	/**
	 * 删除索引
	 * @throws IOException
	 */
	@DeleteMapping("/index")
	public void delIndex() throws IOException {
		//创建索引请求对象
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
		//链接客户端
		IndicesClient client = restHighLevelClient.indices();
		//删除索引
		AcknowledgedResponse delete = client.delete(deleteIndexRequest, RequestOptions.DEFAULT);
		//响应
		boolean acknowledged = delete.isAcknowledged();
		System.out.println(acknowledged);
	}

	/**
	 * 创建文档
	 * @throws IOException
	 */
	@PutMapping("/doc")
	public void addDoc() throws IOException {
		//准备json数据
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "spring cloud实战");
		jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
		jsonMap.put("studymodel", "201001");
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
		jsonMap.put("timestamp", dateFormat.format(new Date()));
		jsonMap.put("price", 5.6f);
		//创建索引请求对象
		IndexRequest indexRequest = new IndexRequest("xc_course");
		//指定索引添加文档
		indexRequest.source(jsonMap);
		//链接客户端
		IndexResponse indexResponse = restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		//获取响应结果
		DocWriteResponse.Result result = indexResponse.getResult();
		System.out.println(result);
	}

	/**
	 * 获取文档
	 * @throws IOException
	 */
	@GetMapping("/doc")
	public void queryDoc() throws IOException {
		GetRequest getRequest = new GetRequest("xc_course","rUhG33UBfL2WVxr4PQZB");
		GetResponse getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
		boolean exists = getResponse.isExists();
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		System.out.println(sourceAsMap);

	}

}
