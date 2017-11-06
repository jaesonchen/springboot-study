package com.asiainfo.biapp.mcd.ec2b.cache;

/*
 * 缓存管理类，用于初始化并获得客户群缓存，可根据需求构建相应的缓存实现
 */
public class CacheManager {

	private static class CacheHolder {
		static ICache<String, Object> cache = new SimpleCache();
	}
	public static ICache<String, Object> getInstance() {
		return CacheHolder.cache;
	}
}