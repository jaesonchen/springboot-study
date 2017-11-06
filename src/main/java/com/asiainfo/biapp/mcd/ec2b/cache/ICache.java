package com.asiainfo.biapp.mcd.ec2b.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
 * 缓存接口，本系统可以使用得缓存都必须实现该接口，或者通过adapter方式实现该接口
 */
public interface ICache<K, V> {

	//增加缓存项
	public void put(K key, V value);
	
	//取得缓存项
	public V get(K key);
	
	//取得缓存项，超过指定时间的返回null
	public V get(K key, long expire, TimeUnit unit);
	
	//删除缓存项
	public void remove(Object key);
	
	//清除整个缓存
	public void clear();
	
	public Set<K> keySet();
	
	public Collection<V> values();
	
	public boolean containsKey(Object key);
	
	public boolean containsValue(Object value);
	
	public int size();

}
