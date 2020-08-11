package com.github.chenlijia1111.util.excel;

import com.github.chenlijia1111.utils.office.excel.ExcelExport;
import com.github.chenlijia1111.utils.office.excel.ExcelImportUtil;
import com.github.chenlijia1111.utils.office.excel.ExcelReplaceUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 陈礼佳
 * @since 2019/9/10 22:31
 */
public class TestExcel {

    @Test
    public void test1() {

        File inputFile = new File("E:\\毕业\\test.xls");
        File outputFile = new File("E:\\毕业\\test.xls");

        HashMap<String, String> map = new HashMap<>();
        map.put("礼", "小小小小");

        ExcelReplaceUtil.doReplace(map, inputFile, outputFile);

    }

    @Test
    public void testImport() {
        File file = new File("E:\\公司资料\\公司\\南天司法\\开发资料\\9.27反馈\\收据导入模板.xlsx");
        List<ImportPojo> importPojos = ExcelImportUtil.importExcel(file, ImportPojo.class, 1);
        System.out.println(importPojos);
    }

    @Test
    public void testExportLargeData() {
        File outputFile = new File("D:\\公司资料\\喜餐\\测试导出大数据.xlsx");
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            //构造数据
            ArrayList<ListVo> list = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                ListVo listVo = new ListVo();
                listVo.setId(i);
                listVo.setCreateTime(new Date());
                listVo.setOperateName("测试");
                listVo.setOperateUserName("测试");
                list.add(listVo);
            }


            new ExcelExport("供应商员工列表.xlsx", ListVo.class).
                    setDataList(list).
                    exportData(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
