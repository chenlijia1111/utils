package com.github.chenlijia1111.util.cn;

import com.github.chenlijia1111.utils.cn.PinYinUtil;
import org.junit.Test;

/**
 * 拼音测试
 *
 * @author 陈礼佳
 * @since 2019/9/19 23:28
 */
public class PinYinTest {

    @Test
    public void test1() {
        String s = "にほんご我是中国人,我叫陈礼佳にほんご";

        String s1 = PinYinUtil.toPinYin(s);
        System.out.println(s1);

        String s2 = PinYinUtil.toFirstPinYin(s);
        System.out.println(s2);
    }

}
