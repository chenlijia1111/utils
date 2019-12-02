package com.github.chenlijia1111.utils.cn.enums;

/**
 * 翻译语言枚举
 * @author 陈礼佳
 * @since 2019/11/30 10:22
 */
public enum  TranslateLanguageEnum {

    ZH_CN("zh-CN","简体中文"),
    EN("en","英文"),
    DE("de","德语"),
    FR("fr","法语"),
    JA("ja","日语"),
    TH("th","泰语"),
    VI("vi","越南语"),
    RU("ru","俄语"),
    ;

    TranslateLanguageEnum(String abbr, String name) {
        this.abbr = abbr;
        this.name = name;
    }

    private String abbr;//语言简写

    private String name;//语言名称

    public String getAbbr() {
        return abbr;
    }

    public String getName() {
        return name;
    }
}
