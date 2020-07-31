package com.github.chenlijia1111.util.core.reflect;

/**
 * @author Chen LiJia
 * @since 2020/7/31
 */
public class Admin {

    private String name;

    private Boolean statue;

    private boolean status1;

    public String getName() {
        return name;
    }

    public Admin setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getStatue() {
        return statue;
    }

    public void setStatue(Boolean statue) {
        this.statue = statue;
    }

    public boolean isStatus1() {
        return status1;
    }

    public void setStatus1(boolean status1) {
        this.status1 = status1;
    }

}
