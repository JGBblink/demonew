package com.example.eureka_client.service;

import cn.hutool.core.util.RandomUtil;
import com.example.eureka_client.dao.entity.UserEntity;
import com.example.eureka_client.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	public List<UserEntity> findUser() {
		return userMapper.findUser();
	}

	public List<UserEntity> insertUser(Long id,String userName) {
		return userMapper.insertUser(id,"111" + RandomUtil.randomInt(100,1000),"2020-11-10","2222",userName,"1","1",new Date(),new Date());
	}

}
