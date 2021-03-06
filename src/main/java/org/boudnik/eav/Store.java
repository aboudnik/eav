package org.boudnik.eav;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 05/12/17 01:07
 */
public class Store {

    private final Map<Object, Map<String, Map<String, Object>>> data = implementation();

    @FunctionalInterface
    public interface Strategy {
        void merge(Map<String, Object> src, Map<String, Object> dst);
    }

    public static final Strategy ADD = (src, dst) -> {
        dst.putAll(src);
    };

    public static final Strategy REPLACE = (src, dst) -> {
        dst.clear();
        ADD.merge(src, dst);
    };

    private <K, V> Map<K, V> implementation() {
        return new HashMap<>();
    }

    public void print() {
        System.out.println(data);
    }

    public void clear() {
        data.clear();
    }

    public void put(Object key, String type, Map<String, Object> values, Strategy strategy) {
        strategy.merge(values, obtain(obtain(data, key), type));
    }

    private <K, T, V> Map<T, V> obtain(Map<K, Map<T, V>> map, K key) {
        Map<T, V> value = map.get(key);
        if (value == null) {
            map.put(key, value = implementation());
        }
        return value;
    }

    public Map<String, Map<String, Object>> get(Object key) {
        return data.get(key);
    }

    public Map<Object, Map<String, Object>> getByType(String type) {
        Map<Object, Map<String, Object>> result = implementation();
        for (Map.Entry<Object, Map<String, Map<String, Object>>> byKey : data.entrySet()) {
            Map<String, Map<String, Object>> value = byKey.getValue();
            for (Map.Entry<String, Map<String, Object>> entry : value.entrySet()) {
                if (entry.getKey().equals(type)) {
                    result.put(byKey.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }
}
