package com.example.eureka_client.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Component
//@Primary
public class DynamicDataSource extends AbstractRoutingDataSource {
	@Autowired
	@Qualifier("selectDataSource")   //获取读的数据源
	private DataSource selectDataSource;

	@Autowired
	@Qualifier("updateDataSource")  //获取写的数据源
	private DataSource updateDataSource;

	/**
	 * 这个是主要的方法，返回的是生效的数据源名称
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		System.out.println("DataSourceContextHolder：：：" + DataSourceContextHolder.getDbType());
		return DataSourceContextHolder.getDbType();
	}

	/**
	 * 配置数据源信息   注册数据源的操作 最终注入到datasource数据源中
	 */
	@Override
	public void afterPropertiesSet() {
		Map<Object, Object> map = new HashMap<>();
		map.put("selectDataSource", selectDataSource);
		map.put("updateDataSource", updateDataSource);
		setTargetDataSources(map);
		setDefaultTargetDataSource(updateDataSource);
		super.afterPropertiesSet();
	}
}
