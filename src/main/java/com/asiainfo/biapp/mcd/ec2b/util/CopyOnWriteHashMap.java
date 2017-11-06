package com.asiainfo.biapp.mcd.ec2b.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/*
 * fail-safe类型HashMap实现，适用于高并发读，少量修改
 */
public class CopyOnWriteHashMap<K, V> implements Cloneable, Map<K, V> {

	final transient ReentrantLock lock = new ReentrantLock();
	private volatile Map<K, V> internalMap;
	private final int DEFAULT = 16;
	
	public CopyOnWriteHashMap() {
        internalMap = new HashMap<K, V>(DEFAULT);
    }
	
	//设置初始化大小，减少hash冲突
	public CopyOnWriteHashMap(int size) {
        internalMap = new HashMap<K, V>(size);
    }
	
	@Override
    public V put(K key, V value) {
		
		final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);	//复制出一个新HashMap
            V val = newMap.put(key, value);						//在新HashMap中执行写操作
            internalMap = newMap;								//将原来的Map引用指向新Map
            return val;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    	
    	final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            newMap.putAll(m);
            internalMap = newMap;
        } finally {
            lock.unlock();
        }
    }

	@Override
	public V remove(Object key) {
		
		final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            V val = newMap.remove(key);
            internalMap = newMap;
            return val;
        } finally {
            lock.unlock();
        }
	}
	
	@Override
	public void clear() {
		
		final ReentrantLock lock = this.lock;
        lock.lock();
        try {
        	internalMap = new HashMap<K, V>();
        } finally {
            lock.unlock();
        }
	}
    
    @Override
    public V get(Object key) {
        return internalMap.get(key);
    }
	@Override
	public int size() {
		return internalMap.size();
	}
	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}
	@Override
	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}
	@Override
	public Set<K> keySet() {
		return internalMap.keySet();
	}
	@Override
	public Collection<V> values() {
		return internalMap.values();
	}
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return internalMap.entrySet();
	}
	@Override
    public Object clone() {
        try {
        	@SuppressWarnings("unchecked")
			CopyOnWriteHashMap<K, V> c = (CopyOnWriteHashMap<K, V>)(super.clone());
            return c;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
}