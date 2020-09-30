package com.example.demo_test.common;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * mock工具类
 *
 * @author JGB、xzy
 */
@Slf4j
public class MockInstanceUtil {

	/**
	 * 生成的list长度
	 */
	private static final int LIST_RANDOM_BOUND = 5;
	/**
	 * 生成的map长度
	 */
	private static final int MAP_RANDOM_BOUND = 5;
	/**
	 * 生成的时间最小值(天)
	 */
	private static final int DATE_RANDOM_BOUND_MIN = -7;
	/**
	 * 生成的时间最大值(天)
	 */
	private static final int DATE_RANDOM_BOUND_MAX = 7;

	/**
	 * mock对象
	 *
	 * @param clazz    : 需要mock对象的class
	 * @param consumer : 处理方法,可以对实例化的对象赋值
	 * @param <T>      : 对象类型
	 * @return
	 */
	public static <T> T mockInstance(Class<T> clazz, Consumer<T> consumer, String... ignoreProperties) {
		T t = mockInstance(clazz, ignoreProperties);
		consumer.accept(t);
		return t;
	}

	/**
	 * mock对象
	 *
	 * @param clazz : 需要mock对象的class
	 * @param <T>   : 对象类型
	 * @return
	 */
	public static <T> T mockInstance(Class<T> clazz, String... ignoreProperties) {
		T t = null;
		try {
			Constructor c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			t = (T) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			log.error(e.getMessage());
		}
		if (t == null) {
			return null;
		}
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
		// 设置对象属性
		Field[] fields = clazz.getDeclaredFields();
		Class superClazz = clazz.getSuperclass();
		//如果有父类，把父类的属性也加入数组中
		if (superClazz != null) {
			Field[] superFields = superClazz.getDeclaredFields();
			Field[] allFields = new Field[fields.length + superFields.length];
			System.arraycopy(fields, 0, allFields, 0, fields.length);
			System.arraycopy(superFields, 0, allFields, fields.length, superFields.length);
			fields = allFields;
		}
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (compareList(field.getGenericType().toString())) {
					// 获取泛型类型
					ParameterizedType p = (ParameterizedType) field.getGenericType();
					Type type1 = p.getActualTypeArguments()[0];
					if (clazz == type1) {
						//如果属性是本身类的list，则不调用mock，否则会造成死循环
						continue;
					}
				}
				if (ignoreList == null || !ignoreList.contains(field.getName())) {
					Type type = field.getGenericType();
					field.set(t, mock(type));
				}
			} catch (IllegalAccessException e) {
				log.error(e.getMessage());
			}
		}

		return t;
	}


	/**
	 * 批量生成mock对象
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> mockInstances(Class<T> clazz, String... ignoreProperties) {
		return mockInstances(clazz, 1, ignoreProperties);
	}

	/**
	 * 批量生成mock对象
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> mockInstances(Class<T> clazz,Consumer<T> consumer, String... ignoreProperties) {
		List<T> ts = mockInstances(clazz, 1, ignoreProperties);
		if(CollectionUtil.isNotEmpty(ts)) {
			ts.forEach(consumer);
		}
		return ts;
	}

	/**
	 * 批量生成mock对象
	 *
	 * @param clazz
	 * @param min   最小生成数量
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> mockInstances(Class<T> clazz, int min, String... ignoreProperties) {
		int size = RandomUtil.randomInt(min, LIST_RANDOM_BOUND);
		List<T> result = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			T o = mockInstance(clazz, ignoreProperties);
			result.add(o);
		}
		return result;
	}

	/**
	 * mock对象属性
	 *
	 * @param type : mock对象的属性类型
	 * @return
	 */
	private static Object mock(Type type) {

		String compareType = type.toString();

		if (compareString(compareType)) {
			// mock字符串
			return "mock";
		} else if (compareByte(compareType)) {
			// byte
			return (byte) RandomUtil.randomInt(127);
		} else if (compareShort(compareType)) {
			// short
			return (short) RandomUtil.randomInt(127);
		} else if (compareInteger(compareType)) {
			// int
			return RandomUtil.randomInt();
		} else if (compareLong(compareType)) {
			// long
			return RandomUtil.randomLong();
		} else if (compareFloat(compareType)) {
			// float
			return (float) RandomUtil.randomDouble(Float.MAX_VALUE);
		} else if (compareDouble(compareType)) {
			// double
			return RandomUtil.randomDouble();
		} else if (compareDate(compareType)) {
			// date
			return RandomUtil.randomDate(new Date(), DateField.DAY_OF_YEAR, DATE_RANDOM_BOUND_MIN, DATE_RANDOM_BOUND_MAX);
		} else if (compareBoolean(compareType)) {
			// boolean
			return RandomUtil.randomBoolean();
		} else if (compareBigDecimal(compareType)) {
			// bigDecimal
			return RandomUtil.randomBigDecimal();
		} else if (compareList(compareType)) {
			// list
			int size = RandomUtil.randomInt(1, LIST_RANDOM_BOUND);
			List list = new ArrayList(size);

			// 获取泛型类型
			ParameterizedType p = (ParameterizedType) type;
			Type type1 = p.getActualTypeArguments()[0];
			for (int i = 0; i < size; i++) {
				list.add(mock(type1));
			}
			return list;
		} else if (compareMap(compareType)) {

			int size = RandomUtil.randomInt(1, MAP_RANDOM_BOUND);
			Map map = new HashMap(size);
			// 获取泛型类型
			ParameterizedType p = (ParameterizedType) type;
			for (int i = 0; i < size; i++) {
				Type type1 = p.getActualTypeArguments()[0];
				Type type2 = p.getActualTypeArguments()[1];
				map.put(mock(type1), mock(type2));
			}
			return map;
		} else if (compareEnum(type)) {
			//枚举类型
			Class clazz = (Class) type;
			return clazz.getEnumConstants()[0];
		} else if (compareShukang(compareType)) {
			// 术康自定义dto
			return mockInstance((Class) type);
		}
		return null;
	}

	private static boolean compareString(String compareType) {
		return compareType.startsWith("class java.lang.String");
	}

	private static boolean compareByte(String compareType) {
		return compareType.startsWith("class java.lang.Byte") || "byte".equals(compareType);
	}

	private static boolean compareShort(String compareType) {
		return compareType.startsWith("class java.lang.Short") || "short".equals(compareType);
	}

	private static boolean compareInteger(String compareType) {
		return compareType.startsWith("class java.lang.Integer") || "int".equals(compareType);
	}

	private static boolean compareLong(String compareType) {
		return compareType.startsWith("class java.lang.Long") || "long".equals(compareType);
	}

	private static boolean compareFloat(String compareType) {
		return compareType.startsWith("class java.lang.Float") || "float".equals(compareType);
	}

	private static boolean compareDouble(String compareType) {
		return compareType.startsWith("class java.lang.Double") || "double".equals(compareType);
	}

	private static boolean compareDate(String compareType) {
		return compareType.startsWith("class java.util.Date");
	}

	private static boolean compareBoolean(String compareType) {
		return compareType.startsWith("class java.lang.Boolean") || "boolean".equals(compareType);
	}

	private static boolean compareBigDecimal(String compareType) {
		return compareType.startsWith("class java.math.BigDecimal");
	}

	private static boolean compareList(String compareType) {
		return compareType.startsWith("java.util.List");
	}

	private static boolean compareMap(String compareType) {
		return compareType.startsWith("interface java.util.Map") || compareType.startsWith("java.util.Map");
	}

	private static boolean compareEnum(Type type) {
		if(type instanceof Class) {
			return ((Class) type).isEnum();
		}
		return type.getClass().isEnum();
	}

	private static boolean compareShukang(String compareType) {
		return compareType.startsWith("class com.shukangyun");
	}
}
