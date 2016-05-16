package com.gongsibao.common.db;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 取得数据源的KEY
 * 
 */
public class DataSourceRouter extends AbstractRoutingDataSource implements ApplicationContextAware {
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(DataSourceRouter.class);
	// 数据源key的存储控制器
	private DataSourceKey dataSourceKey;
	private ApplicationContext applicationContext;

	/**
	 * 获得数据源的key
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		String key = "";
		try {
			key = dataSourceKey.getKey();
		} catch (Throwable e) {
			throw new RuntimeException("get data source key fail", e);
		}
		return key;
	}

	public DataSourceKey getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(DataSourceKey dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}