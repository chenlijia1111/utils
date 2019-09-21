package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.email.EmailHostType;
import com.github.chenlijia1111.utils.email.EmailUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/12 0012 上午 10:36
 **/
public class TestEmail {

    @Test
    public void testSendEmail() {

        EmailUtil emailUtil = new EmailUtil("571740367@qq.com", "xxnollszofsnbfab", EmailHostType.QQ, "陈陈陈礼佳");
        List<File> files = Lists.asList(new File("D:\\image\\s.jpg"));
        Result checkResult = emailUtil.sendMassage(Lists.asList("17770039942@163.com"), null, null, null);
        System.out.println(checkResult);


    }
}
