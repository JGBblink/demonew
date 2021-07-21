package com.example.demo_test.rest;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.json.JSONUtil;
import com.example.demo_test.dto.UserDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/unit")
public class DemoRest implements Delayed{

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


	public static void main(String[] args) throws Exception{
		DelayQueue<DemoRest> delayeds = new DelayQueue<>();

		delayeds.add(new DemoRest("A",5000L));
		delayeds.add(new DemoRest("B",2000L));
		delayeds.add(new DemoRest("C",7000L));

		System.out.println(delayeds.take().getName());

	}

	private long time;
	private long startTime = System.currentTimeMillis();
	private String name;

	public DemoRest(String name,long time) {
		this.time = time;
		this.name = name;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return (startTime + time) - System.currentTimeMillis();
	}

	@Override
	public int compareTo(Delayed o) {
		return (int)(this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
	}
}
