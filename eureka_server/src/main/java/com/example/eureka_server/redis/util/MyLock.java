package com.example.eureka_server.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 自实现redis分布式可重入锁
 * @author JGB
 */
@Slf4j
@Component
public class MyLock {
	
	private final static String lockTemplet = "demo:JGB:lock:%s";
	private final static String innerLockTemplet = "demo:JGB:innerLock:%s";

	@Autowired
	private RedisTemplate redisTemplate;


	/**
	 * 自实现分布式锁:加锁
	 * @param id
	 * @return
	 */
	public boolean lock(String id) {

		// 自旋获取内部锁
		while (!innerLock(id)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String key = String.format(lockTemplet, id);
		Long increment = redisTemplate.opsForValue().increment(key);
		redisTemplate.expire(key,5,TimeUnit.SECONDS);
		log.info("lock" + increment);
		if(log.isDebugEnabled()) {
			log.info(Thread.currentThread().getName() + ":" + increment);
		}
		return increment > 0;
	}

	/**
	 * 自实现分布式锁:解锁
	 * @param id
	 * @return
	 */
	public boolean unlock(String id) {
		Long increment = redisTemplate.opsForValue().decrement(String.format(lockTemplet, id));
		log.info("increment=" + increment);
		if(increment <= 0) {
			innerUnlock(id);
		}
		if(log.isDebugEnabled()) {
			log.info(Thread.currentThread().getName() + ":" + increment);
		}
		return increment <= 0;
	}

	/**
	 * 内部加锁
	 * @param id
	 * @return
	 */
	private boolean innerLock(String id) {
		String key = String.format(innerLockTemplet, id);
		long threadId = Thread.currentThread().getId();
		if(redisTemplate.hasKey(key)) {
			Object o = redisTemplate.opsForValue().get(key);
			if(o != null) {
				Long redisThreadId = Long.parseLong(o.toString());
				if(redisThreadId.equals(threadId)) {
					// 可重入锁
					log.info("----------------");
					return true;
				}
			}
		}

		if(redisTemplate.opsForValue().setIfAbsent(key,threadId)) {
			if(log.isDebugEnabled()) {
				log.info(Thread.currentThread().getName() + ":获取redis锁");
			}
			return true;
		}
		return false;
	}

	/**
	 * 内部解锁
	 * @param id
	 * @return
	 */
	private boolean innerUnlock(String id) {
		// 释放分布式内部锁
		if(redisTemplate.delete(String.format(innerLockTemplet, id))) {
			if(log.isDebugEnabled()) {
				log.info(Thread.currentThread().getName() + ":释放redis锁");
			}
			return true;
		}
		return false;
	}
}
