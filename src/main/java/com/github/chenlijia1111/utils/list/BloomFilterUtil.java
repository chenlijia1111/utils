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

    /**
     * 使用 bitSet 减少空间消耗 https://shimo.im/docs/hwHkWTVJjVjprxDk
     */
    BitSet bitSet = new BitSet(SIZE);

    Hash[] hashArray = new Hash[8];

    //提供八个种子的hash 只有八个hash都是一样的才表示存在，防止hash冲突
    private static final int seeds[] = new int[]{3, 5, 7, 9, 11, 13, 17, 19};

    /**
     * 构造函数
     * 构造 8 个 hash 对象
     */
    public BloomFilterUtil() {
        for (int i = 0; i < seeds.length; i++) {
            hashArray[i] = new Hash(seeds[i]);
        }
    }

    /**
     * 添加数据
     * @param string
     */
    public void add(String string) {
        for (Hash hash : hashArray) {
            bitSet.set(hash.getHash(string), true);
        }
    }

    /**
     * 判断是否包含数据
     * @param string
     * @return
     */
    public boolean contains(String string) {
        boolean have = true;
        for (Hash hash : hashArray) {
            have &= bitSet.get(hash.getHash(string));
        }
        return have;
    }

    /**
     * 移除数据
     * @param string
     */
    public void remove(String string){
        for (Hash hash : hashArray) {
            bitSet.clear(hash.getHash(string));
        }
    }


    /**
     * hash 类，提供生成 hash 值的方法
     */
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
