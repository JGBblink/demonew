package com.example.demo_test.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DemoService {

	public String serviceMethod1() {

		return "service";
	}

	public static void main(String[] args) {
		String input = "[I love byte bytedance] Ilovebytedance";
		String[] split = input.split("]");
		// 获取所有单词字典
		String strs = split[0].substring(1,split[0].length()-1);
		List<String> allDict = Arrays.asList(strs.split(" "));

		// 获取匹配字符串
		String matchStr = split[1].trim();

		System.out.println(allDict);
		System.out.println(matchStr);

		// 匹配

	}

	public boolean match(String matchStr,List<String> dict) {
		// 校验

		// 匹配算法


		return false;
	}
}
