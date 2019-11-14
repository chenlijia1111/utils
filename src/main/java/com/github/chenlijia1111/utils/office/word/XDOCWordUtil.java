package com.github.chenlijia1111.utils.office.word;

import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.list.Lists;
import com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 2007 word工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 5:56
 **/
public class XDOCWordUtil {


    /**
     * 设置一级标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public static void setLevelTitle1(XWPFDocument document, XWPFParagraph paragraph, String text) {
        /**1.将段落原有文本(原有所有的Run)全部删除*/
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        createRun.setFontSize(16);
        createRun.setFontFamily("黑体");
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(20);
        paragraph.setSpacingBefore(20);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        addCustomHeadingStyle(document, "标题1", 1);
        paragraph.setStyle("标题1");
    }


    /**
     * 设置二级标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public static void setLevelTitle2(XWPFDocument document, XWPFParagraph paragraph, String text) {
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        createRun.setFontSize(16);
        createRun.setBold(true);
        createRun.setFontFamily("楷体_GB2312");
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
        addCustomHeadingStyle(document, "标题2", 2);
        paragraph.setStyle("标题2");
    }

    /**
     * 设置正文文本内容及样式
     *
     * @param paragraph
     * @param text
     */
    public static void setTextPro(XWPFParagraph paragraph, String text) {
        deleteRun(paragraph);
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.addBreak();
        createRun.addTab();
        createRun.setFontFamily("仿宋_GB2312");
        createRun.setFontSize(16);

        paragraph.setFirstLineIndent(20);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
    }

    /**
     * 向段落添加文本
     *
     * @param paragraph
     * @param text
     */
    public static void addTextPro(XWPFParagraph paragraph, String text) {
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.createRun();
        createRun.setText(text);
        XWPFRun separtor = paragraph.createRun();
        /**两段之间添加换行*/
        separtor.addBreak();
        createRun.addTab();
        createRun.setFontFamily("仿宋_GB2312");
        createRun.setFontSize(16);
        paragraph.setFirstLineIndent(20);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);

        paragraph.addRun(createRun);
        paragraph.addRun(separtor);
    }

    /**
     * 设置表格标题内容及样式
     *
     * @param paragraph
     * @param text
     */
    public static void setTableOrChartTitle(XWPFParagraph paragraph, String text) {
        /**1.将段落原有文本(原有所有的Run)全部删除*/
        deleteRun(paragraph);
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(text);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        createRun.setFontFamily("楷体");
        createRun.setFontSize(16);
        createRun.setBold(true);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
    }

    public static void deleteRun(XWPFParagraph paragraph) {
        /*1.将段落原有文本(原有所有的Run)全部删除*/
        List<XWPFRun> runs = paragraph.getRuns();
        int runSize = runs.size();
        /*Paragrap中每删除一个run,其所有的run对象就会动态变化，即不能同时遍历和删除*/
        int haveRemoved = 0;
        for (int runIndex = 0; runIndex < runSize; runIndex++) {
            paragraph.removeRun(runIndex - haveRemoved);
            haveRemoved++;
        }
    }


    /**
     * 合并行
     *
     * @param table
     * @param col     需要合并的列
     * @param fromRow 开始行
     * @param toRow   结束行
     */
    public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                vmerge.setVal(STMerge.RESTART);
            } else {
                vmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    /**
     * 设置表格内容居中
     * 该方法应该在表格全部设置完内容之后再进行调用
     *
     * @param table
     * @param tableAlign 表格位置 {@link STJc#LEFT}
     */
    public static void setTableCenter(XWPFTable table,
                                      org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc.Enum tableAlign) {

        //表格居左
        CTTbl ttbl = table.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTJc cTJc = tblPr.addNewJc();
        cTJc.setVal(tableAlign);//表格位置

        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            row.setHeight(400);
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTc cttc = cell.getCTTc();
                CTTcPr ctPr = cttc.addNewTcPr();
                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
            }
        }
    }


    /**
     * 增加自定义标题样式。这里用的是stackoverflow的源码
     *
     * @param docxDocument 目标文档
     * @param strStyleId   样式名称
     * @param headingLevel 样式级别
     */
    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {


        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    /**
     * 将对象数据写入 XWPFTable 中
     * obj 对象需要加入{@link com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField} 注解
     * {@link ExcelExportField#titleHeadName()} 表示列名
     *
     * @param list     1
     * @param table    2
     * @param objClass 3
     * @return void
     * @since 下午 7:38 2019/11/14 0014
     **/
    public static void writePojoToTable(List<? extends Object> list, XWPFTable table, Class objClass) {
        if (Lists.isNotEmpty(list) && Objects.nonNull(objClass) && Objects.nonNull(table)) {
            //获取所有有导出字段的注解
            List<Field> allFields = PropertyUtil.getAllFields(objClass);
            //过滤出含有导出注解的字段
            allFields = allFields.stream().filter(e -> Objects.nonNull(e.getAnnotation(ExcelExportField.class))).collect(Collectors.toList());
            //开始处理
            if (Lists.isNotEmpty(allFields)) {
                List<ExcelExportField> collect = allFields.stream().map(e -> e.getAnnotation(ExcelExportField.class)).sorted(Comparator.comparing(ExcelExportField::sort)).collect(Collectors.toList());
                //制作表头
                XWPFTableRow firstRow = table.getRow(0);
                for (int i = 0; i < collect.size(); i++) {

                    ExcelExportField excelExportField = collect.get(i);

                    XWPFTableCell cell;
                    if (i == 0) {
                        cell = firstRow.getCell(0);
                    } else {
                        cell = firstRow.createCell();
                    }
                    cell.setWidth(excelExportField.cellWidth() + "");
                    cell.setWidthType(TableWidthType.DXA);//自定义宽度
                    //表头
                    cell.setText(excelExportField.titleHeadName());
                }

                for (Object o : list) {
                    //开始处理真正的数据
                    //创建行,从第二行开始，不用再创建列了
                    XWPFTableRow row = table.createRow();
                    for (Field field : allFields) {
                        field.setAccessible(true);
                        ExcelExportField excelExportField = field.getAnnotation(ExcelExportField.class);
                        XWPFTableCell cell = row.getCell(excelExportField.sort());
                        cell.setWidth(excelExportField.cellWidth() + "");
                        cell.setWidthType(TableWidthType.DXA);//自定义宽度
                        try {
                            Object fieldValue = PropertyUtil.getFieldValue(o, objClass, field.getName());
                            if (Objects.nonNull(fieldValue)) {
                                cell.setText(fieldValue.toString());
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

}
