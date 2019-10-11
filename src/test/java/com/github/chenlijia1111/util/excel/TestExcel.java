package com.github.chenlijia1111.util.excel;

import com.github.chenlijia1111.utils.office.excel.ExcelImportUtil;
import com.github.chenlijia1111.utils.office.excel.ExcelReplaceUtil;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @author 陈礼佳
 * @since 2019/9/10 22:31
 */
public class TestExcel {

    @Test
    public void test1(){

        File inputFile = new File("E:\\毕业\\test.xls");
        File outputFile = new File("E:\\毕业\\test.xls");

        HashMap<String, String> map = new HashMap<>();
        map.put("礼","小小小小");

        ExcelReplaceUtil.doReplace(map,inputFile,outputFile);

    }

    @Test
    public void testImport(){
        File file = new File("E:\\公司资料\\公司\\南天司法\\开发资料\\9.27反馈\\收据导入模板.xlsx");
        List<ImportPojo> importPojos = ExcelImportUtil.importExcel(file, ImportPojo.class, 1);
        System.out.println(importPojos);
    }
}
