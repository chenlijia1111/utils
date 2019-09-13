package com.github.chenlijia1111.utils.list;

import com.github.chenlijia1111.utils.list.annos.MapType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * map工具类
 *
 * @author 陈礼佳
 * @since 2019/9/13 8:03
 */
public class Maps {


    public static Maps instance;

    /**
     * 单例
     *
     * @return
     */
    public static Maps getInstance() {
        if (null == instance) {
            instance = new Maps();
        }
        return instance;
    }


    /**
     * 判断 map 是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return null == map || map.size() == 0;
    }

    /**
     * 判断是否非空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }


    /**
     * 创建 map 构建器
     * @param mapType
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> MapBuilder<K, V> mapBuilder(MapType mapType) {
        return getInstance().buildMap(mapType);
    }


    /**
     * 创建 map 构建器
     * 如果map 类型为空 默认返回hashMap构建器
     *
     * @param mapType {@link MapType}
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> MapBuilder<K, V> buildMap(MapType mapType) {
        switch (mapType) {
            case HASH_MAP:
                return new HashMapBuilder<>();
            case LINKED_HASH_MAP:
                return new LinkedHashMapBuilder<>();
            case TREE_MAP:
                return new TreeMapBuilder<>();
        }
        return new HashMapBuilder<>();
    }


    /**
     * map 构建器接口
     *
     * @param <K>
     * @param <V>
     */
    public interface MapBuilder<K, V> {

        MapBuilder put(K key, V value);

        MapBuilder put(Map<K, V> map);

        Map<K, V> build();

    }

    /**
     * hashMap 构建器
     *
     * @param <K>
     * @param <V>
     */
    public class HashMapBuilder<K, V> implements MapBuilder<K, V> {

        private Map<K, V> map;

        public HashMapBuilder() {
            this.map = new HashMap();
        }

        @Override
        public MapBuilder put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        @Override
        public MapBuilder put(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        @Override
        public Map<K, V> build() {
            return this.map;
        }
    }

    /**
     * linkedHashMap 构建器
     *
     * @param <K>
     * @param <V>
     */
    public class LinkedHashMapBuilder<K, V> implements MapBuilder<K, V> {

        private Map<K, V> map;

        public LinkedHashMapBuilder() {
            this.map = new LinkedHashMap();
        }

        @Override
        public MapBuilder put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        @Override
        public MapBuilder put(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        @Override
        public Map<K, V> build() {
            return this.map;
        }
    }


    /**
     * treeMap 构建器
     *
     * @param <K>
     * @param <V>
     */
    public class TreeMapBuilder<K, V> implements MapBuilder<K, V> {

        private Map<K, V> map;

        public TreeMapBuilder() {
            this.map = new TreeMap<>();
        }

        @Override
        public MapBuilder put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        @Override
        public MapBuilder put(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        @Override
        public Map<K, V> build() {
            return this.map;
        }
    }

}
