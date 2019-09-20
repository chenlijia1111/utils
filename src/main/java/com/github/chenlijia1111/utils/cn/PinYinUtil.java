package com.github.chenlijia1111.utils.cn;

import com.github.chenlijia1111.utils.core.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉语拼音工具类
 *
 * @author 陈礼佳
 * @since 2019/9/19 23:21
 */
public class PinYinUtil {


    /**
     * 字符串转 拼音
     * 如果不是中文 直接原文转出
     *
     * @param str
     * @return
     */
    public static String toPinYin(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar > 128) {
                String[] strings = new String[0];
                try {
                    //返回这个汉字的所有读音
                    strings = PinyinHelper.toHanyuPinyinStringArray(aChar, defaultFormat);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                if (null != strings && strings.length > 0) {
                    for (int j = 0; j < strings.length; j++) {
                        //只取第一个就好
                        sb.append(strings[0]);
                    }
                } else {
                    //不是汉字，转不了,原文转出
                    sb.append(aChar);
                }
            } else {
                //在128以内 不是汉字，不用转
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转 拼音首字母
     * 如果不是中文 直接原文转出
     * 如果是中文
     * 则取首字母
     *
     * @param str
     * @return
     */
    public static String toFirstPinYin(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        char aChar = chars[0];
        if (aChar > 128) {
            String[] strings = new String[0];
            try {
                //返回这个汉字的所有读音
                strings = PinyinHelper.toHanyuPinyinStringArray(aChar, defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
            if (null != strings && strings.length > 0) {
                sb.append(strings[0].toCharArray()[0]);
            } else {
                //不是汉字，转不了,原文转出
                sb.append(aChar);
            }
        } else {
            //在128以内 不是汉字，不用转
            sb.append(aChar);
        }
        return sb.toString();
    }


    /**
     * 字符串转 每个字的拼音首字母拼接成的字符串
     * 如果不是中文 直接原文转出
     * 如果是中文
     * 则取首字母
     *
     * @param str
     * @return
     */
    public static String toFirstPinYinArrayStr(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar > 128) {
                String[] strings = new String[0];
                try {
                    //返回这个汉字的所有读音
                    strings = PinyinHelper.toHanyuPinyinStringArray(aChar, defaultFormat);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                if (null != strings && strings.length > 0) {
                    for (int j = 0; j < strings.length; j++) {
                        //只取第一个首字母就好
                        sb.append(strings[0].toCharArray()[0]);
                    }
                } else {
                    //不是汉字，转不了,原文转出
                    sb.append(aChar);
                }
            } else {
                //在128以内 不是汉字，不用转
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

}
