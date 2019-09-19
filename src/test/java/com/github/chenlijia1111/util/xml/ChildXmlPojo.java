package com.github.chenlijia1111.util.xml;

import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2019/9/19 22:49
 */
public class ChildXmlPojo {


    private String size;

    private String color;

    public ChildXmlPojo() {
    }

    public ChildXmlPojo(String size, String color) {
        this.size = size;
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
