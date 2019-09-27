package com.github.chenlijia1111.util.propertyCheck;

import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import com.github.chenlijia1111.utils.core.enums.PropertyCheckType;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/27 0027 下午 1:14
 **/
public class UserInfo {

    @PropertyCheck(name = "姓名")
    private String name;

    @PropertyCheck(name = "手机号", checkType = PropertyCheckType.MOBILE_PHONE)
    private String telephone;

    @PropertyCheck(name = "邮箱", checkType = PropertyCheckType.E_MAIL)
    private String email;

    @PropertyCheck(name = "身份证", checkType = PropertyCheckType.ID_CARD)
    private String idCard;

    public UserInfo() {
    }

    public UserInfo(String name, String email, String idCard,String telephone) {
        this.name = name;
        this.email = email;
        this.idCard = idCard;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
