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
     *
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
        return new MapBuilder<>(mapType);
    }


    /**
     * hashMap 构建器
     *
     * @param <K>
     * @param <V>
     */
    public class MapBuilder<K, V> {

        private Map<K, V> map;

        public MapBuilder(MapType mapType) {
            switch (mapType) {
                case LINKED_HASH_MAP:
                    this.map = new LinkedHashMap<>();
                    break;
                case TREE_MAP:
                    this.map = new TreeMap<>();
                    break;
                default:
                    this.map = new HashMap<>();
            }
        }

        public MapBuilder put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        public MapBuilder put(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        public Map<K, V> build() {
            return this.map;
        }
    }


}
