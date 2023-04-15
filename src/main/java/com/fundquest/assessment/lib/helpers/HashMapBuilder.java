package com.fundquest.assessment.lib.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

public class HashMapBuilder<K, V> {
    private Map<K, V> entries;

    public HashMapBuilder() {
        this.entries = new LinkedHashMap<>();
    }

    public HashMapBuilder<K, V> entry(K key, V value) {
        entries.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return this.entries;
    }

}
