package com.github.chenlijia1111.util.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.xml.XmlUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 陈礼佳
 * @since 2019/9/15 12:56
 */
public class XMLTest {

    @Test
    public void test1() {
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

    @Test
    public void test2() {
        ArrayList<XMLPojo> pojos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            XMLPojo xmlPojo = new XMLPojo("name" + i, "code" + i);
            ArrayList<ChildXmlPojo> childXmlPojos = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ChildXmlPojo childXmlPojo = new ChildXmlPojo("size" + j, "color" + j);
                childXmlPojos.add(childXmlPojo);
            }
            xmlPojo.setChildXmlPojoList(childXmlPojos);

            pojos.add(xmlPojo);
        }

        String s = JSONUtil.objToStr(pojos);
        System.out.println(s);

        String s1 = XmlUtil.parseJsonToXml(s, "chenlijia");
        System.out.println(s1);


        XMLPojo xmlPojo = new XMLPojo("name", "code");
        ArrayList<ChildXmlPojo> childXmlPojos = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            ChildXmlPojo childXmlPojo = new ChildXmlPojo("size" + j, "color" + j);
            childXmlPojos.add(childXmlPojo);
        }
        xmlPojo.setChildXmlPojoList(childXmlPojos);

        String s2 = JSONUtil.objToStr(xmlPojo);
        System.out.println(s2);

        String s3 = XmlUtil.parseJsonToXml(s2, "chenlijia");
        System.out.println(s3);
    }
}
