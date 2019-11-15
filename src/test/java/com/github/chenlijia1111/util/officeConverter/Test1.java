package com.github.chenlijia1111.util.officeConverter;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.office.LibreOfficeConvertUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/15 0015 下午 1:39
 **/
public class Test1 {


    @Test
    public void test3() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("cmd.exe /c  \"C:\\Program Files\\LibreOffice\\program\\soffice.exe\" --convert-to html -outdir E:\\公司资料\\公司\\恩德生态\\开发资料\\123456.html E:\\公司资料\\公司\\恩德生态\\开发资料\\恩德奖励说明.docx");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {

        File file = new File("E:\\公司资料\\公司\\恩德生态\\开发资料\\恩德奖励说明.docx");
        LibreOfficeConvertUtil.convert("C:\\Program Files\\LibreOffice\\program","html","E:\\公司资料\\公司\\恩德生态\\开发资料",file);

    }


}
