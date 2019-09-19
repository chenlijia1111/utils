package com.github.chenlijia1111.util.xml;

import java.util.List;

/**
 * @author 陈礼佳
 * @since 2019/9/19 22:48
 */
public class XMLPojo {

    private String name;

    private String code;

    private List<ChildXmlPojo> childXmlPojoList;

    public XMLPojo() {
    }

    public XMLPojo(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ChildXmlPojo> getChildXmlPojoList() {
        return childXmlPojoList;
    }

    public void setChildXmlPojoList(List<ChildXmlPojo> childXmlPojoList) {
        this.childXmlPojoList = childXmlPojoList;
    }
}
