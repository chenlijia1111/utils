package com.github.chenlijia1111.utils.list;

import java.util.BitSet;

/**
 * 布隆过滤
 *
 * @author 陈礼佳
 * @since 2020/4/4 8:57
 */
public class BloomFilterUtil {

    private static final int SIZE = 1 << 24;

    //使用 bitSet 减少空间消耗 https://shimo.im/docs/hwHkWTVJjVjprxDk
    BitSet bitSet = new BitSet(SIZE);

    Hash[] hashArray = new Hash[8];

    //提供八个种子的hash 只有八个hash都是一样的才表示存在，防止hash冲突
    private static final int seeds[] = new int[]{3, 5, 7, 9, 11, 13, 17, 19};

    public BloomFilterUtil() {
        for (int i = 0; i < seeds.length; i++) {
            hashArray[i] = new Hash(seeds[i]);
        }
    }

    public void add(String string) {
        for (Hash hash : hashArray) {
            bitSet.set(hash.getHash(string), true);
        }
    }

    public boolean contains(String string) {
        boolean have = true;
        for (Hash hash : hashArray) {
            have &= bitSet.get(hash.getHash(string));
        }
        return have;
    }


    class Hash {
        private int seed = 0;

        public Hash(int seed) {
            this.seed = seed;
        }

        public int getHash(String string) {
            int val = 0;
            int len = string.length();
            for (int i = 0; i < len; i++) {
                val = val * seed + string.charAt(i);
            }
            return val & (SIZE - 1);
        }
    }

}
