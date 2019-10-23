package com.github.chenlijia1111.util.word;

import com.github.chenlijia1111.utils.list.Maps;
import com.github.chenlijia1111.utils.list.annos.MapType;
import com.github.chenlijia1111.utils.office.word.WordUtils;
import org.junit.Test;

import java.io.File;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/22 0022 上午 9:33
 **/
public class WordTest {

    @Test
    public void test1(){
        File file = new File("E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表.docx");
        System.out.println(file.exists());

        WordUtils.replaceWord("E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表.docx","E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表1.docx", Maps.mapBuilder(MapType.HASH_MAP).put("{委托方}","陈礼佳").build());
    }

}
