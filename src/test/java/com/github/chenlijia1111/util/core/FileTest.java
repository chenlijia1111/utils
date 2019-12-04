package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.WebFileUtil;
import com.github.chenlijia1111.utils.core.enums.SystemPropertyEnum;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 6:32
 **/
public class FileTest {

    @Test
    public void test1() {
        try {
            long l = System.currentTimeMillis();
            FileUtils.copyFile(new File("D:\\ssmProject\\waibao\\pageoffice\\target\\pageOffice.war"), new File("D:\\ssmProject\\waibao\\pageoffice\\target\\pageOffice1.war"));
            System.out.println(System.currentTimeMillis() - l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试七牛云上传文件
     */
    @Test
    public void testQiNiuUpload() {
        long l = System.currentTimeMillis();
        File file = new File("C:\\Users\\Administrator\\Desktop\\picture\\19_3_20_3.jpg");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);
            WebFileUtil.qiNiuUpload(inputStream, file.getName());
            System.out.println((System.currentTimeMillis() - l) / 1000);
            IOUtil.close(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSystemProperty() {
        SystemPropertyEnum[] values = SystemPropertyEnum.values();
        for (SystemPropertyEnum value : values) {
            String property = System.getProperty(value.getName());
            System.out.println(value.getName() + ":" + property);
        }
    }

}
