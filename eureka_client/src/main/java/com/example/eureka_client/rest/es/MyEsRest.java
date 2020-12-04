package com.example.eureka_client.rest.es;

import cn.hutool.core.date.DateUtil;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author: JGB
 */
@RestController
@RequestMapping("/es/demo")
public class MyEsRest {

	@Autowired
	RestHighLevelClient client;

	/**
	 * 创建索引
	 * @throws IOException
	 */
	@PostMapping("/index")
	public void delIndex() throws IOException{
		// 指定索引
		IndexRequest request = new IndexRequest("jgbtest");

		// 设置doc数据
		//request.id("1");
		String jsonString = "{" +
				"\"description\":\"kimchy\"," +
				"\"name\":\"2013-01-30\"," +
				"\"studymodel\":\"trying out Elasticsearch\"" +
				"}";
		request.source(jsonString, XContentType.JSON);

		// 发送请求
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		System.out.println(indexResponse.toString());

		// 解析结果
		String id = indexResponse.getId();
		String index = indexResponse.getIndex();
		if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
			// 第一次创建
			System.out.println(index + " : id" + id + " 第一次创建");
		} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
			// 不是第一次则更新
			System.out.println(index + " : id" + id + " 已存在则更新");
		}
		// 分区信息
		ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

		}
		if (shardInfo.getFailed() > 0) {
			for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
				String reason = failure.reason();
				System.out.println(reason);
			}
		}
	}

	@GetMapping("/doc")
	public void getDoc(String docId) throws Exception{
		// 根据index下doc指定id获取对应doc
		GetRequest getRequest = new GetRequest("jgbtest", docId);

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

		System.out.println(getResponse.toString());
		String index = getResponse.getIndex();
		String id = getResponse.getId();
		if (getResponse.isExists()) {
			long version = getResponse.getVersion();
			String sourceAsString = getResponse.getSourceAsString();
			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
			byte[] sourceAsBytes = getResponse.getSourceAsBytes();

			System.out.println(id + " : " + index + " : " + sourceAsString);
		} else {
			System.out.println("不存在");
		}
	}

	@DeleteMapping("/doc")
	public void delDoc(String docId) throws Exception{
		DeleteRequest getRequest = new DeleteRequest("jgbtest", docId);
		DeleteResponse deleteResponse = client.delete(getRequest, RequestOptions.DEFAULT);

		String index = deleteResponse.getIndex();
		String id = deleteResponse.getId();
		long version = deleteResponse.getVersion();
		ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
			System.out.println("删除成功");
		}
		if (shardInfo.getFailed() > 0) {
			for (ReplicationResponse.ShardInfo.Failure failure :
					shardInfo.getFailures()) {
				String reason = failure.reason();
			}
		}
	}

	@PutMapping("/doc")
	public void updateDoc(String docId) throws Exception{
		UpdateRequest request = new UpdateRequest("jgbtest", docId);
		String date = DateUtil.formatDate(new Date());
		String jsonString = "{" +
				"\"name\":" + date + "" +
				"}";
		request.doc(jsonString, XContentType.JSON);

		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

		String index = response.getIndex();
		String id = response.getId();
		long version = response.getVersion();
		ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
			System.out.println("更新成功");
		}
		if (shardInfo.getFailed() > 0) {
			for (ReplicationResponse.ShardInfo.Failure failure :
					shardInfo.getFailures()) {
				String reason = failure.reason();
			}
		}
	}


}
