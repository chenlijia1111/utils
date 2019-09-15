package com.github.chenlijia1111.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.xml.XmlUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author 陈礼佳
 * @since 2019/9/15 12:56
 */
public class XMLTest {

    @Test
    public void test1(){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("E:\\chenlijiaProject\\maven\\test.xml"));
            Map<String, Object> stringObjectMap = XmlUtil.parseXMLToMap(fileInputStream);
            System.out.println(stringObjectMap);
            ObjectMapper objectMapper = new ObjectMapper();
            String string = objectMapper.writeValueAsString(stringObjectMap);
            System.out.println(string);
        } catch (FileNotFoundException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
