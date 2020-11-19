package com.example.eureka_client.rest.mycat;

import cn.hutool.core.util.RandomUtil;
import com.example.eureka_client.dao.entity.UserEntity;
import com.example.eureka_client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * mycat测试
 * @author: JGB
 */
@RestController
@RequestMapping("/mycat")
public class MyCatRest {

	@Autowired
	UserService userService;


	@GetMapping("/get")
	public List<UserEntity> testMycatSelect() {
		List<UserEntity> user = userService.findUser();
		return user;
	}

	@GetMapping("/insert")
	public List<UserEntity> testMycatInsert(String name) {
		List<UserEntity> user = userService.insertUser(RandomUtil.randomLong(0,Long.MAX_VALUE),name + RandomUtil.randomInt(0,10000));
		return user;
	}
}
