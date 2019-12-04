package com.github.chenlijia1111.utils.core.commonCheckFunction;

import com.github.chenlijia1111.utils.common.constant.RegConstant;
import com.github.chenlijia1111.utils.core.StringUtils;
import org.joda.time.DateTime;

import java.util.function.Predicate;

/**
 * 身份证号校验
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/3 0003 下午 6:26
 **/
public class IDCardNoCheck implements Predicate<String> {


    @Override
    public boolean test(String idNumber) {
        if (StringUtils.isNotEmpty(idNumber)) {
            // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
            //假设18位身份证号码:41000119910101123X  410001 19910101 123X
            //^开头
            //[1-9] 第一位1-9中的一个      4
            //\\d{5} 五位数字           10001（前六位省市县地区）
            //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
            //\\d{2}                    91（年份）
            //((0[1-9])|(10|11|12))     01（月份）
            //(([0-2][1-9])|10|20|30|31)01（日期）
            //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
            //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
            //$结尾

            //假设15位身份证号码:410001910101123  410001 910101 123
            //^开头
            //[1-9] 第一位1-9中的一个      4
            //\\d{5} 五位数字           10001（前六位省市县地区）
            //\\d{2}                    91（年份）
            //((0[1-9])|(10|11|12))     01（月份）
            //(([0-2][1-9])|10|20|30|31)01（日期）
            //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
            //$结尾


            boolean matches = idNumber.matches(RegConstant.ID_CARD);

            //判断第18位校验值
            if (matches) {

                //开始判断月份的天数
                if (idNumber.length() == 18) {
                    //获取年 月 日
                    String year = idNumber.substring(6, 10);
                    String month = idNumber.substring(10, 12);
                    String day = idNumber.substring(12, 14);
                    DateTime dateTime = new DateTime(Integer.valueOf(year), Integer.valueOf(month), 1, 0, 0);
                    //获取这个月的最大天数,判断最大日期是否正确
                    int maximumValue = dateTime.dayOfMonth().getMaximumValue();
                    if (Integer.valueOf(day) > maximumValue) {
                        return false;
                    }
                }

                //开始判断月份的天数 15位的身份证都是99年以前的
                if (idNumber.length() == 15) {
                    //获取年 月 日
                    String year = idNumber.substring(6, 8);
                    String month = idNumber.substring(8, 10);
                    String day = idNumber.substring(10, 12);
                    DateTime dateTime = new DateTime(Integer.valueOf("19" + year), Integer.valueOf(month), 1, 0, 0);
                    //获取这个月的最大天数,判断最大日期是否正确
                    int maximumValue = dateTime.dayOfMonth().getMaximumValue();
                    if (Integer.valueOf(day) > maximumValue) {
                        return false;
                    }
                }

                //根据算法计算最后一位加权因子是否正确
                if (idNumber.length() == 18) {
                    try {
                        char[] charArray = idNumber.toCharArray();
                        //前十七位加权因子
                        int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                        //这是除以11后，可能产生的11位余数对应的验证码
                        String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                        int sum = 0;
                        for (int i = 0; i < idCardWi.length; i++) {
                            int current = Integer.parseInt(String.valueOf(charArray[i]));
                            int count = current * idCardWi[i];
                            sum += count;
                        }
                        char idCardLast = charArray[17];
                        int idCardMod = sum % 11;
                        if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                            return true;
                        } else {
//                            System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
//                                    "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                            return false;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }

            }
            return matches;
        }

        return false;
    }
}
