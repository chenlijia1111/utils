package com.github.chenlijia1111.utils.database;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.list.Lists;

import java.io.*;
import java.util.List;

/**
 * mysql 备份工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/23 0023 上午 10:36
 **/
public class MysqlBackUtil {


    /**
     * 导出sql
     * window里面 /表示是命令参数后缀,比如  format /q
     * \是cmd命令中的路径分隔符 比如C:\Users\SYSTEM32
     * 所以路径符号不要用 / 替换 \\
     * window 命令 cmd.exe /c "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqldump.exe" -h192.168.1.134 -P3306 -uroot -proot expertise>D:\\1023.sql
     * linux 命令 /usr/bin/mysqldump -h192.168.1.134 -P3306 -uroot -proot expertise>/home/nantian/1023.sql
     *
     * @param mysqlBinPath mysql 安装的bin 路径 用于获取mysql的命令 示例C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin 或者 /usr/bin
     * @param ip
     * @param port         端口
     * @param userName     mysql用户名
     * @param password     mysql密码
     * @param databaseName 数据库名
     * @param ignoreTables 要忽略导出的表
     * @param exportFile   导出到的文件
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 上午 10:39 2019/10/23 0023
     **/
    public static Result exportSql(String mysqlBinPath, String ip, String port, String userName, String password,
                                   String databaseName, List<String> ignoreTables, File exportFile) {

        try {
            //判断exportFile的文件夹是否存在,不存在则创建
            FileUtils.checkDirectory(exportFile.getParent());
            return exportSql(mysqlBinPath, ip, port, userName, password, databaseName, ignoreTables, new BufferedOutputStream(new FileOutputStream(exportFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Result.failure("操作失败");
    }


    /**
     * 导出sql
     * window里面 /表示是命令参数后缀,比如  format /q
     * \是cmd命令中的路径分隔符 比如C:\Users\SYSTEM32
     * 所以路径符号不要用 / 替换 \\
     * window 命令 cmd.exe /c "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqldump.exe" -h192.168.1.134 -P3306 -uroot -proot expertise>D:\\1023.sql
     * linux 命令 /usr/bin/mysqldump -h192.168.1.134 -P3306 -uroot -proot expertise>/home/nantian/1023.sql
     *
     * @param mysqlBinPath       mysql 安装的bin 路径 用于获取mysql的命令 示例C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin 或者 /usr/bin
     * @param ip
     * @param port               端口
     * @param userName           mysql用户名
     * @param password           mysql密码
     * @param databaseName       数据库名
     * @param ignoreTables       要忽略导出的表
     * @param exportOutputStream 导出到输出流
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 上午 10:39 2019/10/23 0023
     **/
    public static Result exportSql(String mysqlBinPath, String ip, String port, String userName, String password,
                                   String databaseName, List<String> ignoreTables, OutputStream exportOutputStream) {

        StringBuilder cmd = new StringBuilder();

        String property = System.getProperty("os.name").toLowerCase(); //Windows 10

        //windows 系统
        if (property.startsWith("win")) {
            //处理mysqlBinPath
            //路径使用\\
            mysqlBinPath = mysqlBinPath.replaceAll("/", "\\");
            //如果路径最后面有\,截掉,后面统一加路径符号
            mysqlBinPath = mysqlBinPath.endsWith("\\") ? mysqlBinPath.substring(0, mysqlBinPath.length() - 1) : mysqlBinPath;
            //开始拼接命令
            cmd.append("cmd.exe /c ");
            cmd.append("\"" + mysqlBinPath + "\\mysqldump.exe\" ");
        } else {
            //路径使用/
            mysqlBinPath = mysqlBinPath.replaceAll("\\\\", "/");
            //如果路径最后面有/,截掉,后面统一加路径符号
            mysqlBinPath = mysqlBinPath.endsWith("/") ? mysqlBinPath.substring(0, mysqlBinPath.length() - 1) : mysqlBinPath;
            cmd.append(mysqlBinPath + "/mysqldump ");
        }


        cmd.append("-h" + ip + " ");
        cmd.append("-P" + port + " ");
        cmd.append("-u" + userName + " ");
        cmd.append("-p" + password + " ");
        cmd.append(databaseName + " ");
        if (Lists.isNotEmpty(ignoreTables)) {
            for (String ignoreTable : ignoreTables) {
                cmd.append("--ignore-table=" + databaseName + "." + ignoreTable + " ");
            }
        }

        Runtime runtime = Runtime.getRuntime();

        try {
            Process exec = runtime.exec(cmd.toString());
            InputStream is = exec.getInputStream();
            IOUtil.writeInputStream(is, exportOutputStream);
            return Result.success("操作成功");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure("导出失败");
    }


    /**
     * 导入sql
     * window里面 /表示是命令参数后缀,比如  format /q
     * \是cmd命令中的路径分隔符 比如C:\Users\SYSTEM32
     * 所以路径符号不要用 / 替换 \\
     * window 命令 cmd.exe /c "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" -h192.168.1.134 -P3306 -uroot -proot expertise<D:\\1023.sql
     * linux 命令 /usr/bin/mysql -h192.168.1.134 -P3306 -uroot -proot expertise</home/nantian/1023.sql
     * <p>
     * mysql 导入这个命令中因为不需要使用输入流,所以是异步执行的,
     * 想要实现同步执行就需要开两个线程消费掉输入流
     *
     * @param mysqlBinPath mysql 安装的bin 路径 用于获取mysql的命令 示例C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin 或者 /usr/bin
     * @param ip
     * @param port         端口
     * @param userName     mysql用户名
     * @param password     mysql密码
     * @param databaseName 数据库名
     * @param sqlFilePath  sql文件地址
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 上午 10:39 2019/10/23 0023
     **/
    public static Result importSql(String mysqlBinPath, String ip, String port, String userName, String password,
                                   String databaseName, String sqlFilePath) {

        StringBuilder cmd = new StringBuilder();

        String property = System.getProperty("os.name").toLowerCase(); //Windows 10

        //windows 系统
        if (property.startsWith("win")) {
            //处理mysqlBinPath
            //路径使用\\
            mysqlBinPath = mysqlBinPath.replaceAll("/", "\\");
            //如果路径最后面有\,截掉,后面统一加路径符号
            mysqlBinPath = mysqlBinPath.endsWith("\\") ? mysqlBinPath.substring(0, mysqlBinPath.length() - 1) : mysqlBinPath;
            //开始拼接命令
            cmd.append("cmd.exe /c ");
            cmd.append("\"" + mysqlBinPath + "\\mysql.exe\" ");
        } else {
            //路径使用/
            mysqlBinPath = mysqlBinPath.replaceAll("\\\\", "/");
            //如果路径最后面有/,截掉,后面统一加路径符号
            mysqlBinPath = mysqlBinPath.endsWith("/") ? mysqlBinPath.substring(0, mysqlBinPath.length() - 1) : mysqlBinPath;
            cmd.append(mysqlBinPath + "/mysql ");
        }


        cmd.append("-h" + ip + " ");
        cmd.append("-P" + port + " ");
        cmd.append("-u" + userName + " ");
        cmd.append("-p" + password + " ");
        cmd.append(databaseName);
        cmd.append("<" + sqlFilePath);

        Runtime runtime = Runtime.getRuntime();

        try {
            Process process = runtime.exec(cmd.toString());
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();

            IOUtil.ConsumeInputStream consumeInputStream = new IOUtil.ConsumeInputStream(inputStream);
            IOUtil.ConsumeInputStream consumeInputStream1 = new IOUtil.ConsumeInputStream(errorStream);
            consumeInputStream.start();
            consumeInputStream1.start();

            process.waitFor();

            //执行完成--终止线程
            consumeInputStream.interrupt();
            consumeInputStream1.interrupt();

            process.destroy();
            return Result.success("操作成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Result.failure("导入失败");
    }


}
