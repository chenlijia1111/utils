package com.github.chenlijia1111.util.list;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.SwatchStopUtil;
import org.junit.Test;

import java.nio.LongBuffer;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * @author 陈礼佳
 * @since 2020/4/4 9:17
 */
public class BitSetTest {

    @Test
    public void test1(){

        SwatchStopUtil swatchStopUtil = new SwatchStopUtil();

        BitSet bitSet = new BitSet(100000000);
        System.out.println(bitSet.size());
        System.out.println(bitSet.length());

        boolean b = bitSet.get(1);
        System.out.println(b);

        bitSet.set(1);
        System.out.println(bitSet.get(1));
        bitSet.set(3);
        bitSet.set(65);
        bitSet.set(66);

        String s = JSONUtil.objToStr(bitSet.toLongArray());
        System.out.println(s);

        System.out.println(bitSet.toString());
        swatchStopUtil.watch();

        List<Long> longs = JSONUtil.strToList(s, ArrayList.class, long.class);
        System.out.println(longs);

        long[] longs1 = longs.stream().mapToLong(e -> e.longValue()).toArray();
        BitSet bitSet1 = BitSet.valueOf(longs1);
        System.out.println(Objects.equals(bitSet,bitSet1));

        swatchStopUtil.stopAndPrint();
    }

}
