package com.github.chenlijia1111.utils.office;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * LibreOffice 转换工具类
 * 经测试，效果良好
 * 但是前提是windows和linux都需要安装LibreOffice
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/15 0015 下午 2:45
 **/
public class LibreOfficeConvertUtil {

    /**
     * 文件格式转化工具
     *
     * @param libreOfficePath libreOfficePath安装路径 如 C:\Program Files\LibreOffice\program
     * @param convertType     要转化的文件类型 如 html pdf
     * @param outDir          输出的路径
     * @param file            要转化的文件
     * @return com.github.chenlijia1111.utils.common.Result
     * @since 下午 3:59 2019/11/15 0015
     **/
    public static Result convert(String libreOfficePath, String convertType, String outDir, File file) {
        StringBuilder cmd = new StringBuilder();

        String property = System.getProperty("os.name").toLowerCase(); //Windows 10

        //windows 系统
        if (property.startsWith("win")) {
            //libreOfficePath
            //路径使用\\
            libreOfficePath = libreOfficePath.replaceAll("/", "\\");
            //如果路径最后面有\,截掉,后面统一加路径符号
            libreOfficePath = libreOfficePath.endsWith("\\") ? libreOfficePath.substring(0, libreOfficePath.length() - 1) : libreOfficePath;
            //开始拼接命令
            cmd.append("cmd.exe /c ");
            cmd.append("\"" + libreOfficePath + "\\soffice.exe\" ");
        } else {
            //路径使用/
            libreOfficePath = libreOfficePath.replaceAll("\\\\", "/");
            //如果路径最后面有/,截掉,后面统一加路径符号
            libreOfficePath = libreOfficePath.endsWith("/") ? libreOfficePath.substring(0, libreOfficePath.length() - 1) : libreOfficePath;
            cmd.append(libreOfficePath + "/soffice ");
        }


        cmd.append("--convert-to " + convertType + " ");
        cmd.append("-outdir " + outDir + " ");
        cmd.append(file.getAbsolutePath());

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

        return Result.failure("转化失败");
    }

}
