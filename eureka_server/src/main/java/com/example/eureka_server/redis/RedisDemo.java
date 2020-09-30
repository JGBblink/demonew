package com.example.eureka_server.redis;

import cn.hutool.core.util.RandomUtil;
import com.example.eureka_server.redis.util.MyLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * redis缓存示例
 *
 * @author: JGB
 */
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisDemo {

	private final static String redisTemplet = "demo:JGB:%s";
	private final static String seckillTemplet = "demo:JGB:seckill:%s";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private MyLock lock;


	/**
	 * demo:演示设置对应key的缓存
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/demo")
	public String redisSetDemo(@RequestParam String key) throws Exception {

		if (redisTemplate.hasKey(redisTemplet)) {
			redisTemplate.delete(redisTemplet);
			System.out.println("delete");
		}
		key = String.format(redisTemplet, key);
		redisTemplate.opsForValue().set(key, key + "demo");
		System.out.println(key);
		return "success";
	}

	/**
	 * demo:演示获取对应key的缓存
	 *
	 * @param key
	 * @return
	 */
	@GetMapping("/demo")
	public String redisGetDemo(@RequestParam String key) {
		String result = (String) redisTemplate.opsForValue().get(String.format(redisTemplet, key));
		return result;
	}

	/**
	 * redis秒杀演示:准备秒杀数据
	 *
	 * @param total
	 */
	@PostMapping("/seckill/ready")
	public void seckill(@RequestParam Integer total) {

		// 商品结构
		Map<String, Object> goods = new HashMap(1);
		goods.put("total", total);
		goods.put("status", 1);
		goods.put("alloc", 0);
		goods.put("users", new ArrayList());

		redisTemplate.opsForHash().putAll(String.format(seckillTemplet, "goods_1"), goods);
	}

	ExecutorService executorService = Executors.newFixedThreadPool(15);

	/**
	 * redis秒杀演示:开始秒杀
	 * 1.采用自实现的redis分布式锁(可重入)
	 *
	 * @return
	 */
	@GetMapping("/seckill/start")
	public Boolean seckillStart() {
		// 秒杀前准备

		// 开始秒杀
		//boolean result = seckill();
		//boolean result = seckillTest();
		luaSeckill();
		// 秒杀后处理

		return false;
	}

	private boolean seckillTest() {
		for (int i = 0; i < 10000; i++) {
			CompletableFuture.runAsync(() -> seckill(), executorService);
		}
		return false;
	}

	/**
	 * 秒杀核心
	 *
	 * @return
	 */
	private boolean seckill() {
		String key = String.format(seckillTemplet, "goods_1");
		int total = (Integer) redisTemplate.opsForHash().get(key, "total");
		int alloc = (Integer) redisTemplate.opsForHash().get(key, "alloc");
		Boolean result = false;
		if (alloc < total) {
			lock.lock("goods_1");
			try {
				total = (Integer) redisTemplate.opsForHash().get(key, "total");
				alloc = (Integer) redisTemplate.opsForHash().get(key, "alloc");
				log.info("获取锁total：" + total + " alloc:" + alloc);
				if (alloc < total) {
					// 验证可重入性
					log.info("得到秒杀资格");
					try {
						lock.lock("goods_1");
					} finally {
						lock.unlock("goods_1");
					}

					// 秒杀成功,被秒杀数+1
					if (log.isDebugEnabled()) {
						log.info(Thread.currentThread().getName() + "获取锁,更新redis...");
					}
					List users = (List) redisTemplate.opsForHash().get(key, "users");
					users.add(RandomUtil.randomInt(1000));
					redisTemplate.opsForHash().put(key, "users", users);
					redisTemplate.opsForHash().increment(key, "alloc", 1);
					result = true;
				}
			} finally {
				log.info("释放锁");
				lock.unlock("goods_1");
			}
		}
		return result;
	}

	public void luaSeckill() {
		String key = String.format(seckillTemplet, "goods_1");
		Object resultAlloc = runLua("lua/seckill2.lua", Object.class, Arrays.asList(key), "10","20");
		System.out.println(resultAlloc);
		/*Integer alloc = Integer.parseInt(resultAlloc);
		if(alloc != null && alloc > 0) {
			System.out.println("秒杀成功");
		}else {
			System.out.println("秒杀失败");
		}*/
	}

	private  <T> T runLua(String fileClasspath, Class<T> returnType, List<String> keys, Object... values) {
		DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(fileClasspath)));
		redisScript.setResultType(returnType);
		//T result = stringRedisTemplate.execute(redisScript, keys, values);
		Object result = redisTemplate.execute(redisScript, keys, values);
		/*Object result = redisTemplate.execute(redisScript, (RedisSerializer<?>) redisTemplate.getKeySerializer(), (RedisSerializer<String>)
				redisTemplate.getKeySerializer(), Lists.newArrayList(keys),values);*/
		return (T)result;
	}
}
