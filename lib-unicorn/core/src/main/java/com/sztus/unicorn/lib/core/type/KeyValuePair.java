package com.sztus.unicorn.lib.core.type;

/**
 * 键值对类型定义
 * @param <K> 键数据类型
 * @param <V> 值数据类型
 */
public class KeyValuePair<K, V> {

    public K getKey() {
        return this.key;
    }
    public V getValue() {
        return this.value;
    }

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    private K key;
    private V value;
}
