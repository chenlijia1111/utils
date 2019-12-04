package com.github.chenlijia1111.utils.dateTime;

import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.commonCheckFunction.IDCardNoCheck;
import com.github.chenlijia1111.utils.list.Lists;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 生日工具类
 *
 * @author 陈礼佳
 * @since 下午 5:58 2019/12/3 0003
 **/
public class BirthUtils {

    //生肖
    public static final String[] ZODIAC_ARR = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    /**
     * 星座日期分布
     *
     * @since 下午 6:15 2019/12/3 0003
     **/
    public static final List<Constellation> CONSTELLATION_LIST = Lists.asList(
            new Constellation("摩羯座", "01.20", "02.18"),
            new Constellation("双鱼座", "02.19", "03.20"),
            new Constellation("白羊座", "03.21", "04.19"),
            new Constellation("金牛座", "04.20", "05.20"),
            new Constellation("双子座", "05.21", "06.21"),
            new Constellation("巨蟹座", "06.22", "07.22"),
            new Constellation("狮子座", "07.23", "08.22"),
            new Constellation("处女座", "08.23", "09.22"),
            new Constellation("天秤座", "09.23", "10.23"),
            new Constellation("天蝎座", "10.24", "11.22"),
            new Constellation("射手座", "11.23", "12.21")
    );


    //星座内部类
    static class Constellation {

        public Constellation(String name, String startDayOfMonth, String endDayOfMonth) {
            this.name = name;
            this.startDayOfMonth = startDayOfMonth;
            this.endDayOfMonth = endDayOfMonth;
        }

        public Constellation() {
        }

        //星座名称
        private String name;

        //开始日期
        private String startDayOfMonth;

        //结束日期
        private String endDayOfMonth;

        public String getName() {
            return name;
        }

        public String getStartDayOfMonth() {
            return startDayOfMonth;
        }

        public String getEndDayOfMonth() {
            return endDayOfMonth;
        }
    }


    /**
     * 根据日期获取生肖
     *
     * @return
     */
    public static String zodiac(LocalDate date) {
        return Objects.isNull(date) ? null : ZODIAC_ARR[date.getYear() % 12];
    }

    /**
     * 根据身份证号判断用户生肖
     * 41000119910101123X 18位
     * 410001910101123 15位
     *
     * @param cardNo
     * @return
     */
    public static String zodiac(String cardNo) {
        if (StringUtils.isNotEmpty(cardNo)) {
            //根据身份证获取生日
            LocalDate birthDay = birthDay(cardNo);
            if (Objects.nonNull(birthDay)) {
                //判断生肖
                return zodiac(birthDay);
            }
        }
        return null;
    }

    /**
     * 根据日期获取星座
     *
     * @param date
     * @return
     */
    public static String constellation(LocalDate date) {
        if (Objects.nonNull(date)) {
            String format = date.toString("MM.dd");
            Optional<Constellation> any = CONSTELLATION_LIST.stream().filter(e -> format.compareTo(e.getStartDayOfMonth()) >= 0 &&
                    format.compareTo(e.getEndDayOfMonth()) <= 0).findAny();
            if (any.isPresent()) {
                return any.get().name;
            }
        }
        return null;
    }

    /**
     * 根据身份证号判断用户星座
     *
     * @param cardNo 身份证号
     * @return
     */
    public static String constellation(String cardNo) {
        if (StringUtils.isNotEmpty(cardNo)) {
            //根据身份证获取生日
            LocalDate birthDay = birthDay(cardNo);
            if (Objects.nonNull(birthDay)) {
                //判断星座
                return constellation(birthDay);
            }
        }
        return null;
    }

    /**
     * 根据身份证号判断用户性别
     * 41000119910101123X 18位 第十七位奇数代表男，偶数代表女
     * 410001910101123 15位 第十五位奇数代表男，偶数代表女
     *
     * @param cardNo
     * @return
     */
    public static String sex(String cardNo) {
        if (StringUtils.isNotEmpty(cardNo)) {
            String sexStr = "0";
            if (cardNo.length() == 15) {
                sexStr = cardNo.substring(14, 15);
            } else if (cardNo.length() == 18) {
                sexStr = cardNo.substring(16, 17);
            }
            int sexNo = Integer.parseInt(sexStr);
            return sexNo % 2 == 0 ? "女" : "男";
        }
        return null;
    }

    /**
     * 根据身份证判断年龄
     *
     * @param cardNo 身份证号
     * @return int
     * @since 上午 9:34 2019/12/4 0004
     **/
    public static int age(String cardNo) {
        return age(cardNo, LocalDate.now());
    }

    /**
     * 根据身份证判断年龄
     *
     * @param cardNo 身份证号
     * @param date   计算年龄的日期
     * @return int
     * @since 上午 9:34 2019/12/4 0004
     **/
    public static int age(String cardNo, LocalDate date) {
        if (StringUtils.isNotEmpty(cardNo) && Objects.nonNull(date)) {
            //根据身份证获取生日
            LocalDate birthDay = birthDay(cardNo);
            if (Objects.nonNull(birthDay)) {
                //计算年龄
                Years years = Years.yearsBetween(birthDay, date);
                return years.getYears();
            }
        }
        return 0;
    }

    /**
     * 根据身份证号获取生日日期
     *
     * @param cardNo 1
     * @return org.joda.dateTime.LocalDate
     * @since 上午 9:40 2019/12/4 0004
     **/
    public static LocalDate birthDay(String cardNo) {
        //先判断身份证号是否合法
        if (new IDCardNoCheck().test(cardNo)) {
            //开始解析
            if (cardNo.length() == 18) {
                String substring = cardNo.substring(6, 14);
                LocalDate localDate = DateTimeConvertUtil.strToLocalDate(substring, "yyyyMMdd");
                return localDate;
            } else if (cardNo.length() == 15) {
                String substring = cardNo.substring(6, 12);
                LocalDate localDate = DateTimeConvertUtil.strToLocalDate(substring, "yyMMdd");
                return localDate;
            }
        }
        return null;
    }

}
