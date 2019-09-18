package com.github.chenlijia1111.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2019/9/18 23:12
 */
public class TestJson {


    @Test
    public void test1(){
        String a = "asd";
        ObjectMapper objectMapper = new ObjectMapper();
        String string = null;
        try {
            string = objectMapper.writeValueAsString(a);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(string);
    }
}
