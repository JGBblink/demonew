package com.example.eureka_server.redis;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * bitmap演示:位图,提供与\或\非\异或等操作
 * 结构:
 * default--> [index_0:false,index_1:false,index_2:false]
 * set(1)-->  [index_0:false,index_1:true,index_2:false]
 *
 * @author JGB
 */
@RestController
@RequestMapping("/redis")
public class RedisBitMap {

	private final static String redisTemplet = "demo:JGB:bloomfilter:%s";

	@Autowired
	RedisTemplate redisTemplate;

	@PostMapping("/bloomfilter/ready")
	public void test1() {
		Boolean xxx = redisTemplate.opsForValue().setBit(String.format(redisTemplet,"bloomfilter"), 5L, true);
		System.out.println(xxx);
	}

	@RequestMapping("/bloomfilter")
	public void test2(String value) {
		String key = String.format(redisTemplet,"bloomfilter");
		int[] hashIndex = getHashs(value,1000);

		// 通过pipeline,发送批量指令
		byte[] serializeKey = redisTemplate.getKeySerializer().serialize(key);
		List<Boolean> list = redisTemplate.executePipelined((RedisCallback) connection -> {
			System.out.println(Arrays.toString(hashIndex));
			for (int i = 0; i < hashIndex.length; i++) {
				connection.setBit(serializeKey, hashIndex[i], true);
			}
			return null;
		});

		System.out.println(list);
		if(list.contains(false)) {
			System.out.println("不存在");
		}
	}

	/**
	 * 范围
	 */
	private static final int cap = 1 << 29;
	/**
	 * hash因子:hash个数
	 */
	private int[] seeds = new int[]{3, 5, 7, 11, 13, 31, 37, 61};

	/**
	 * 获取多个hash
	 * @param value
	 * @param mod
	 * @return
	 */
	private int[] getHashs(String value,int mod) {
		int[] hashs = new int[seeds.length];
		for(int i = 0; i < hashs.length; i++) {
			hashs[i] = hash(value, seeds[i],mod);
		}
		return hashs;
	}

	/**
	 * 计算hash-->(使用Stringhash算法)
	 * @param value
	 * @param seed
	 * @param mod
	 * @return
	 */
	private int hash(String value, int seed,int mod) {
		int result = 0;
		int length = value.length();
		for (int i = 0; i < length; i++) {
			result = seed * result + value.charAt(i);
		}
		return Math.floorMod((cap - 1) & result,mod);
	}

	public static void main(String[] args) {
		RedisBitMap test = new RedisBitMap();
		test.testBloomFilter();
	}

	/**
	 * 简单实现布隆过滤器
	 */
	private void testBloomFilter() {
		int mod = 100000;
		BitSet bitSet = new BitSet(mod);

		int count = 0;
		for (int i = 0; i < 100; i ++) {
			String value = "mock:" + RandomUtil.randomInt(1000);
			boolean[] result = check(bitSet,mod,value);
			if(contain(result)) {
				count++;
			}else {
				System.out.println(value +  Arrays.toString(result));
			}
		}

		System.out.println(count);
		System.out.println(bitSet);

	}

	/**
	 * 校验是否存在
	 * @param result
	 * @return
	 */
	private boolean contain(boolean[] result) {
		for (int i = 0; i < result.length; i++) {
			if(!result[i]) {
				return true;
			}
		}
		return false;
	}

	private boolean[] check(BitSet bitSet,int mod,Object value) {
		int[] hashs = getHashs(JSONUtil.toJsonStr(value),mod);
		boolean[] result = new boolean[hashs.length];
		for(int i = 0; i < hashs.length; i++) {
			int index = hashs[i];
			if(bitSet.get(index)) {
				result[i] = true;
			}else {
				result[i] = false;
				bitSet.set(index);
			}
		}
		return result;
	}

	/**
	 * 简单使用位图
	 */
	private void testBitMap() {
		BitSet bitSet = new BitSet();
		int randomInt = RandomUtil.randomInt(100, 1000);
		for (int i = 0; i < randomInt; i++) {
			int index = RandomUtil.randomInt(100, 1000);
			if (bitSet.get(index)) {
				set(bitSet, i);
				continue;
			}
			bitSet.set(index);
		}
		System.out.println(randomInt);
		System.out.println(bitSet.cardinality());
		System.out.println(bitSet.size());
	}

	/**
	 * 递归调用:可能导致stack调用链过长异常
	 *
	 * @param bitSet
	 * @param i
	 */
	private static void set(BitSet bitSet, int i) {
		if (bitSet.get(i)) {
			set(bitSet, i + 1);
		} else {
			bitSet.set(i);
		}
	}
}
