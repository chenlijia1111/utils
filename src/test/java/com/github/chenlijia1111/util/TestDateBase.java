package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.database.MysqlBackUtil;
import com.github.chenlijia1111.utils.database.MysqlDataDictonaryUtil;
import org.junit.Test;

import java.io.*;
import java.sql.SQLException;
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
                "3306", "root", "root", "demo", null, new File("D:/1023.sql"));
    }

    @Test
    public void test4() {
        MysqlBackUtil.importSql("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin", "192.168.1.134",
                "3306", "root", "root", "demo", "C:\\upload\\file\\backup\\98f758c88d5347928a036af3d5e2b86e.sql");
    }

    @Test
    public void test5() {
        String property = System.getProperty("os.name");
        System.out.println(property);
    }

    //导出数据字典
    @Test
    public void test6() {
        try {
            MysqlDataDictonaryUtil mysqlDataDictonaryUtil = new MysqlDataDictonaryUtil();
            mysqlDataDictonaryUtil.writeToWord("jdbc:mysql://58.250.17.31:33306/expertise?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL",
                    "root", "0822myljsw", "expertise", new File("E:\\公司资料\\公司\\南天司法\\南天数据字典.docx"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
