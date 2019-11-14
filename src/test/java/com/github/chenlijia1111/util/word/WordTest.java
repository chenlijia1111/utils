package com.github.chenlijia1111.util.word;

import com.github.chenlijia1111.utils.list.Maps;
import com.github.chenlijia1111.utils.list.annos.MapType;
import com.github.chenlijia1111.utils.office.word.WordUtils;
import com.github.chenlijia1111.utils.office.word.XDOCWordUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/22 0022 上午 9:33
 **/
public class WordTest {

    @Test
    public void test1() {
        File file = new File("E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表.docx");
        System.out.println(file.exists());

        WordUtils.replaceWord("E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表.docx", "E:\\公司资料\\公司\\南天司法\\开发资料\\退款申请表1.docx", Maps.mapBuilder(MapType.HASH_MAP).put("{委托方}", "陈礼佳").build());
    }

    @Test
    public void test2() {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream outputStream = new FileOutputStream(new File("E:\\公司资料\\公司\\南天司法\\test1.docx"))) {

            //添加标题
            XWPFParagraph titleParagraph = document.createParagraph();
            //添加一级标题
            XDOCWordUtil.setLevelTitle1(document, titleParagraph, "数据字典");

            //开始添加表格
            //添加一级标题
            XWPFParagraph tableParagraph = document.createParagraph();
            XDOCWordUtil.setLevelTitle2(document, tableParagraph, "表格1");

            XWPFParagraph tableParagraph1 = document.createParagraph();
            XmlCursor cursor = tableParagraph1.getCTP().newCursor();
            XWPFTable table = document.insertNewTbl(cursor);


            //表格插入数据
            //默认会有一个表格,上面的行创建了列，下面就不用创建列了
            XWPFTableRow row = table.getRow(0);

            row.getCell(0).setText("名字");
            row.createCell().setText("性别");
            row.createCell().setText("长度");
            row.createCell().setText("大小");

            XWPFTableRow row1 = table.createRow();
            row1.getCell(0).setText("1");
            row1.getCell(1).setText("1");
            row1.getCell(2).setText("1");
            row1.getCell(3).setText("1");


            XDOCWordUtil.setTableCenter(table, STJc.LEFT);

            document.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
