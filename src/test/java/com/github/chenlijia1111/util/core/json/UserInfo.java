package com.github.chenlijia1111.util.core.json;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/19 0019 上午 8:58
 **/
public class UserInfo {

    private String name;

    private String age;

    public UserInfo(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
