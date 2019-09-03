package com.github.chenlijia1111.utils.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 集合工具列
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 1:14
 **/
public class Lists {

    /**
     * @return java.util.ArrayList
     * @author chenlijia
     * @since 下午 1:51 2019/5/22 0022
     **/
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }


    /**
     * 将数组转换为集合
     *
     * @param ts 1
     * @author chenlijia
     * @since 上午 11:12 2019/6/22 0022
     **/
    public static <T> List<T> asList(T... ts) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (Objects.nonNull(ts)) {
            for (int i = 0; i < ts.length; i++) {
                arrayList.add(ts[i]);
            }
        }
        return arrayList;
    }


    /**
     * 检测集合是否为空
     *
     * @param list 1
     * @author chenlijia
     * @since 下午 4:24 2019/5/22 0022
     **/
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }


    /**
     * 检测集合是否为非空
     *
     * @param list 1
     * @author chenlijia
     * @since 上午 10:13 2019/6/5 0005
     **/
    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

}
