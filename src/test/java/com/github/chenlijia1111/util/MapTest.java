package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.list.Maps;
import com.github.chenlijia1111.utils.list.annos.MapType;
import org.junit.Test;

import java.util.Map;

/**
 * @author 陈礼佳
 * @since 2019/9/13 8:41
 */
public class MapTest {

    @Test
    public void test1() {
        Map build = Maps.mapBuilder(MapType.TREE_MAP).put("s", "s").put("aa", "sa").build();
        System.out.println(build);
    }

}
