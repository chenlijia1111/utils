package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.YamlUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * @author Chen LiJia
 * @since 2020/4/2
 */
public class TestReadYaml {

    @Test
    public void test1(){
        try {
            FileInputStream inputStream = new FileInputStream(new File("D:\\java\\projects\\utils\\src\\test\\java\\com\\github\\chenlijia1111\\util\\core\\testyaml.yml"));
            Properties properties = new YamlUtil().yamlToProperties(inputStream);
            System.out.println(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
