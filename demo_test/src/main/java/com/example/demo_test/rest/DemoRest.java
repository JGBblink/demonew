package com.example.demo_test.rest;

import cn.hutool.json.JSONUtil;
import com.example.demo_test.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unit")
public class DemoRest {

	@Value("${test.properties.demo1}")
	String testDemo;

	String testDemo2;

	@GetMapping("/method1")
	public String unitMethod1(@RequestParam(name = "name",required = true) String name) {
		return "name:" + name + " env:" + testDemo + " field:" + testDemo2;
	}

	@GetMapping("/method2")
	public String unitMethod2(@RequestBody UserDto userDto) {
		return JSONUtil.toJsonStr(userDto);
	}
}
