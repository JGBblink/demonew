package com.example.eureka_client.dao.mapper;

import com.example.eureka_client.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface UserMapper {
	@Select("SELECT name FROM user")
	List<UserEntity> findUser();

	@Select("insert into user(`id`,`auth_id`, `birthday`, `mobile`, `name`, `sex`, `status`, `gmt_create`, `gmt_modified`) " +
			"values (#{id},#{authId},#{birthday},#{mobile},#{name},#{sex},#{status},#{gmtCreate},#{gmtModified})")
	List<UserEntity> insertUser(
			@Param("id") Long id,
			@Param("authId") String authId,
			@Param("birthday") String birthday,
			@Param("mobile") String mobile,
			@Param("name") String name,
			@Param("sex") String sex,
			@Param("status") String status,
			@Param("gmtCreate") Date gmtCreate,
			@Param("gmtModified") Date gmtModified);
}
