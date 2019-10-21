package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.core.IOUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/18 0018 下午 4:36
 **/
public class TestDateBase {


    @Test
    public void test1(){
        //mysqldump -h127.0.0.1 -P3306 -uroot -proot test>C:\chengzheming\backupDatabase\test-20190516.sql  备份
        //mysqldump -h127.0.0.1 -P3306 -uroot -proot test<C:\chengzheming\backupDatabase\test-20190516.sql  恢复

        Runtime runtime = Runtime.getRuntime();

        String cmd = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump -h192.168.1.134 -P3306 -uroot -proot expertise";
        try {
            Process exec = runtime.exec(cmd);
            InputStream inputStream = exec.getInputStream();
            String s = IOUtil.readToString(inputStream);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
