package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.http.HttpClientUtils;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.util.Map;

/**
 * @author Chen LiJia
 * @since 2020/3/17
 */
public class TestHttp {

    @Test
    public void test1() {
        Map map = HttpClientUtils.getInstance(true).putHeader(HTTP.CONTENT_TYPE, "application/json").putHeader("Authorization", "Basic YXBpVXNlcjplMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZQ==").
                putParams("pushid", "20200316154903943").
                putParams("cards", "89860111665100167250,89860111665100167268").doPost("https://39.100.134.137:18443/UUWiFiWSv2/api/pushcards").toMap();
        System.out.println(map);
    }

}
