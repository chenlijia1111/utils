package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.core.IOUtil;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * 测试 nio 读写文件
 *
 * @author Chen LiJia
 * @since 2020/9/22
 */
public class TestNioFile {

    //349 ms
    @Test
    public void test1() {

        try {
            File file = new File("D:\\soft\\JetBrainsCLion.rar");
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel channel = fileInputStream.getChannel();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\soft\\JetBrainsCLion123456.rar"));
            FileChannel channel1 = fileOutputStream.getChannel();
            channel.transferTo(0, file.length(), channel1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //713 ms
    @Test
    public void test2() {

        File file = new File("D:\\soft\\JetBrainsCLion.rar");
        File file1 = new File("D:\\soft\\JetBrainsCLion56789.rar");

        IOUtil.writeFile(file, file1);

    }

}
