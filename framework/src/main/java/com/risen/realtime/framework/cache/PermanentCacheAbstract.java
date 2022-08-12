package com.risen.realtime.framework.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/6/30 10:28
 */
public abstract class PermanentCacheAbstract<K, V> implements Serializable {

    private Integer maximumSize = 1024;
    private Integer initialCapacity = 32;

    private static List<PermanentCacheAbstract> implTree = new ArrayList<>();

    public Cache<K, V> cache;

    public PermanentCacheAbstract(Integer initialCapacity) {
        implTree.add(this);
        Optional.ofNullable(initialCapacity).ifPresent(s -> {
            this.initialCapacity = s;
        });
        cache = Optional.ofNullable(cache).orElse(Caffeine.newBuilder()
                .expireAfterWrite(Long.MAX_VALUE, TimeUnit.DAYS)
                .initialCapacity(this.initialCapacity)
                .maximumSize(this.maximumSize)
                .build());
    }


    public abstract void loadCache();

    public V get(K key) {
        return cache.getIfPresent(key);
    }

    public void put(K key, V obj) {
        cache.put(key, obj);
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    public void removeAll() {
        cache.invalidateAll();
    }


    public Boolean containKey(K key) {
        Predicate<V> predicate = s -> ObjectUtils.isEmpty(s);
        V t = cache.getIfPresent(key);
        if (predicate.test(t)) {
            return false;
        }
        return true;
    }

    public Map<K, V> getAllCacheValue() {
        return cache.asMap();
    }

    public static List<PermanentCacheAbstract> getImplTree() {
        return implTree;
    }
}
