package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.database.MysqlBackUtil;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/18 0018 下午 4:36
 **/
public class TestDateBase {


    /**
     * window里面 /表示是命令参数后缀,比如  format /q
     * \是cmd命令中的路径分隔符 比如C:\Users\SYSTEM32
     * 所以路径符号不要用 / 替换 \\
     *
     * @return void
     * @since 上午 9:28 2019/10/23 0023
     **/
    @Test
    public void test1() {
        //mysqldump -h127.0.0.1 -P3306 -uroot -proot test>C:\chengzheming\backupDatabase\test-20190516.sql  备份
        //mysqldump -h127.0.0.1 -P3306 -uroot -proot test<C:\chengzheming\backupDatabase\test-20190516.sql  恢复

        //"C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe" -h192.168.1.134 -P3306 -uroot -proot expertise>D:\\1023.sql

        Runtime runtime = Runtime.getRuntime();

        String cmd = "cmd.exe /c \"C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe\" -h192.168.1.134 -P3306 -uroot -proot expertise";
//        String cmd = "cmd.exe /c ping 192.168.134";
        try {
            Process exec = runtime.exec(cmd);
            InputStream is = exec.getInputStream();
            String s = IOUtil.readToString(is);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {

        ProcessBuilder processBuilder = new ProcessBuilder();

        List<String> list = new ArrayList<>();
//        list.add("cmd.exe");
//        list.add("/c");
        list.add("mysqldump");
        list.add("-h192.168.1.134");
        list.add("-P3306");
        list.add("-uroot");
        list.add("-proot");
        list.add("expertise");

        ProcessBuilder command = processBuilder.command(list);
        try {
            Process process = command.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        MysqlBackUtil.exportSql("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin", "192.168.1.134",
                "3306", "root", "root", "demo", true, new File("D:/1023.sql"));
    }

    @Test
    public void test4() {
        MysqlBackUtil.importSql("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin", "192.168.1.134",
                "3306", "root", "root", "demo", true, "D:\\1023.sql");
    }

}
