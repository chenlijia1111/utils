package com.github.chenlijia1111.util.wx;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.oauth.wx.WXAppletsLoginUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * 小程序
 * @author Chen LiJia
 * @since 2020/3/2
 */
public class AppletsTest {

    @Test
    public void test1(){
        WXAppletsLoginUtil wxAppletsLoginUtil = WXAppletsLoginUtil.getInstance();
        String accessToken = wxAppletsLoginUtil.accessToken("wx00c16777c69e16d3", "2aff2c72e7d0d14adcad7d861ccb4181");
        System.out.println(accessToken);
    }


    @Test
    public void test2(){
        WXAppletsLoginUtil wxAppletsLoginUtil = WXAppletsLoginUtil.getInstance();
        String accessToken = wxAppletsLoginUtil.accessToken("wx00c16777c69e16d3", "2aff2c72e7d0d14adcad7d861ccb4181");
        System.out.println(accessToken);


        InputStream inputStream = wxAppletsLoginUtil.appletsQrCode("wx00c16777c69e16d3", "2aff2c72e7d0d14adcad7d861ccb4181", null, "123");
        if(Objects.nonNull(inputStream)){
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(new File("D:\\公司资料\\社交商城小程序\\资料\\小程序码\\test1.jpg"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            IOUtil.writeInputStream(inputStream,outputStream);
        }
    }

}
