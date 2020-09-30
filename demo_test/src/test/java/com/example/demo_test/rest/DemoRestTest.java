package com.example.demo_test.rest;

import cn.hutool.json.JSONUtil;
import com.example.demo_test.common.BaseRestTest;
import com.example.demo_test.common.MockInstanceUtil;
import com.example.demo_test.dto.UserDto;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * rest层单元测试
 */
@WebMvcTest(DemoRest.class)
@TestPropertySource(properties = {
		"test.properties.demo1=test"
})
public class DemoRestTest extends BaseRestTest {

	/**
	 * 单元测试rest层
	 * 1.get调用
	 * 2.非body参数
	 * 3.只验证请求调用成功
	 * 4.使用@Value注入的属性
	 * @see TestPropertySource
	 * @throws Exception
	 */
	@Test
	public void unitMethod1() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/unit/method1")
				.param("name","JGB")
		).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	/**
	 * 单元测试rest层
	 * 1.get调用
	 * 2.body json参数
	 * 3.只验证请求调用成功
	 * 4.对返回json进行处理
	 * @throws Exception
	 */
	@Test
	public void unitMethod2() throws Exception{
		UserDto userDto = MockInstanceUtil.mockInstance(UserDto.class);
		mockMvc.perform(MockMvcRequestBuilders.get("/unit/method2")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONUtil.toJsonStr(userDto))
		).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("address").exists())
				.andReturn();
	}

}