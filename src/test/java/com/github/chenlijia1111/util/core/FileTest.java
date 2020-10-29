package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.enums.SystemPropertyEnum;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    @Test
    public void testFileLength() {
        File file = new File("D:\\公司资料\\社交商城小程序\\项目代码\\adminHtml.zip");
        System.out.println(file.length());
    }


    /**
     * 测试传统复制文件耗时
     * 869ms
     */
    @Test
    public void test2() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File file = new File("D:\\soft\\CLion-2019.3.4.exe");
        IOUtil.writeFile(file, new File("D:\\soft\\CLion-2019.3.4-001.exe"));

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 测试 nio 零拷贝方式复制文件耗时
     * 235ms
     */
    @Test
    public void test3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File file = new File("D:\\soft\\CLion-2019.3.4.exe");

        try {

            FileChannel readChannel = FileChannel.open(Paths.get("D:\\soft\\CLion-2019.3.4.exe"), StandardOpenOption.READ);
            FileChannel writeChannel = FileChannel.open(Paths.get("D:\\soft\\CLion-2019.3.4-002.exe"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            readChannel.transferTo(readChannel.position(), file.length(), writeChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


}
