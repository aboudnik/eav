package org.boudnik.eav;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 05/12/17 01:07
 */
public class Store {

    public static final Strategy ADD = (src, dst) -> {
        dst.putAll(src);
    };

    public static final Strategy REPLACE = (src, dst) -> {
        dst.clear();
        ADD.merge(src, dst);
    };

    private final Map<Object, Map<String, Map<String, Object>>> data = new HashMap<>();

    private <K, V> Map<K, V> implementation() {
        return new HashMap<>();
    }

    public void print() {
        System.out.println(data);
    }

    public void clear() {
        data.clear();
    }

    public void put(Object key, String type, Map<String, Object> map, Strategy strategy) {
        Map<String, Map<String, Object>> bucket = obtain(data, key);
        Map<String, Object> cell = obtain(bucket, type);
        strategy.merge(map, cell);
    }

    private <K, T, V> Map<T, V> obtain(Map<K, Map<T, V>> map, K key) {
        Map<T, V> value = map.get(key);
        if (value == null) {
            map.put(key, value = implementation());
        }
        return value;
    }

    /**
     * @author Alexandre_Boudnik
     * @since 05/12/17 02:15
     */
    @FunctionalInterface
    public static interface Strategy {
        void merge(Map<String, Object> src, Map<String, Object> dst);

    }
}
