package com.example.demo_test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * service单元测试
 * @author JGB
 */
@SpringBootTest(classes = DemoService.class)
@RunWith(SpringRunner.class)
public class DemoServiceTest {

	@Autowired
	DemoService demoService;

	@Test
	public void serviceMethod1() {

		String s = demoService.serviceMethod1();
		Assert.assertNotNull(s);
	}
}