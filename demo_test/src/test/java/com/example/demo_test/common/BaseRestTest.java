package com.example.demo_test.common;

import com.example.demo_test.config.TestWebSecurityConfigurer;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 基础单元测试
 * @author JGB
 */
@RunWith(SpringRunner.class)
@Import(value = {TestWebSecurityConfigurer.class})
public abstract class BaseRestTest {
	@Autowired
	protected MockMvc mockMvc;

	@Before
	public void init() {
		System.out.println("单元测试init...");
	}

}
