package com.github.chenlijia1111.utils.list;

import java.util.HashSet;
import java.util.Set;

/**
 * set工具类
 *
 * @author 陈礼佳
 * @since 2019/9/12 22:30
 */
public class Sets {

    /**
     * 判断set集合是否为空
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Set<T> set) {
        return null == set || set.size() == 0;
    }

    /**
     * 判断set集合是否为非空
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(Set<T> set) {
        return !isEmpty(set);
    }


    /**
     * 数组转 set 集合
     *
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> Set<T> asSets(T... ts) {
        HashSet<T> set = new HashSet<>();
        if (null != ts && ts.length > 0) {
            for (int i = 0; i < ts.length; i++) {
                set.add(ts[i]);
            }
        }
        return set;
    }

}
