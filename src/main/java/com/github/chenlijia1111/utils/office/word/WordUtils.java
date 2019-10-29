package com.github.chenlijia1111.utils.office.word;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/5/21 0021 下午 2:44
 **/
public class WordUtils {


    /**
     * 替换word数据
     *
     * @param filePath    源文件
     * @param outFilePath 输出文件
     * @param params      替换的参数  {替换的内容} ---> 替换后的内容
     * @since 下午 3:48 2019/5/28 0028
     **/
    public static Result replaceWord(String filePath, String outFilePath, Map<String, String> params) {

        File file = new File(filePath);
        if (!file.exists())
            return Result.failure("源文件不存在");


        boolean b = FileUtils.checkFileSuffix(filePath, ".docx", ".doc");
        boolean b1 = FileUtils.checkFileSuffix(outFilePath, ".docx", ".doc");
        if (!b || !b1)
            return Result.failure("文件名不合法");

        File outFile = new File(outFilePath);
        File parentFile = outFile.getParentFile();
        if (!parentFile.exists())
            return Result.failure("输出目录不存在");

        if (FileUtils.checkFileSuffix(filePath, ".docx")) {
            return replaceWord07(file, outFile, params);
        }
        if (FileUtils.checkFileSuffix(filePath, ".doc")) {
            return replaceWord03(file, outFile, params);
        }
        return Result.success("格式不合法");
    }

    /**
     * 替换word数据
     *
     * @param fileName     文件名称
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param params       替换的参数
     * @return expertise.common.pojo.Result
     * @author chenlijia
     * @Description TODO
     * @Date 下午 3:48 2019/5/28 0028
     **/
    public static Result replaceWord(String fileName, FileInputStream inputStream, FileOutputStream outputStream, Map<String, String> params) {


        if (FileUtils.checkFileSuffix(fileName, ".docx")) {
            return replaceWord07(inputStream, outputStream, params);
        }
        if (FileUtils.checkFileSuffix(fileName, ".doc")) {
            return replaceWord03(inputStream, outputStream, params);
        }
        return Result.success("格式不合法");
    }


    /**
     * 替换03版的word数据
     *
     * @param file    源文件
     * @param outFile 输出文件
     * @param params  替换的参数
     * @since 下午 3:48 2019/5/28 0028
     **/
    private static Result replaceWord03(File file, File outFile, Map<String, String> params) {

        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {

            return replaceWord03(fileInputStream, fileOutputStream, params);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success("替换成功");
    }


    /**
     * 替换03版的word数据
     *
     * @param inputStream  源文件
     * @param outputStream 输出文件
     * @param params       替换的参数
     * @since 下午 3:48 2019/5/28 0028
     **/
    private static Result replaceWord03(FileInputStream inputStream, FileOutputStream outputStream, Map<String, String> params) {

        try {
            HWPFDocument document = new HWPFDocument(inputStream);
            Range range = document.getRange();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            document.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success("替换成功");
    }

    /**
     * 替换07般的word数据
     * 在模板中填写参数的时候，先在txt文本中写好参数，然后复制到word中去，不然很有可能参数被runs给隔开，从而替换失败
     *
     * @param file    源文件
     * @param outFile 输出文件
     * @param params  替换的参数
     * @since 下午 3:48 2019/5/28 0028
     **/
    private static Result replaceWord07(File file, File outFile, Map<String, String> params) {

        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {

            return replaceWord07(fileInputStream, fileOutputStream, params);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success("替换成功");
    }

    /**
     * 替换07般的word数据
     * 在模板中填写参数的时候，先在txt文本中写好参数，然后复制到word中去，不然很有可能参数被runs给隔开，从而替换失败
     *
     * @param inputStream  源文件
     * @param outputStream 输出文件
     * @param params       替换的参数
     * @since 下午 3:48 2019/5/28 0028
     **/
    private static Result replaceWord07(FileInputStream inputStream, FileOutputStream outputStream, Map<String, String> params) {

        try {

            XWPFDocument document = new XWPFDocument(OPCPackage.open(inputStream));
            //处理段落
            Iterator<XWPFParagraph> paragraphsIterator = document.getParagraphsIterator();
            while (paragraphsIterator.hasNext()) {
                XWPFParagraph paragraph = paragraphsIterator.next();
                dealRuns(paragraph, params);


            }

            //处理表格
            Iterator<XWPFTable> tablesIterator = document.getTablesIterator();
            while (tablesIterator.hasNext()) {
                XWPFTable table = tablesIterator.next();
                //获取行数
                int numberOfRows = table.getNumberOfRows();
                for (int i = 0; i < numberOfRows; i++) {
                    XWPFTableRow row = table.getRow(i);
                    //获取列数
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    for (XWPFTableCell tableCell : tableCells) {
                        //获取cell中的段落数据
                        List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                        for (int j = 0; null != paragraphs && j < paragraphs.size(); j++) {
                            XWPFParagraph paragraph = paragraphs.get(j);
                            //跟上面差不多的处理
                            dealRuns(paragraph, params);

                        }
                    }
                }
            }
            document.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Result.success("替换成功");
    }


    /**
     * 处理runs 进行替换
     *
     * @param paragraph 1
     * @param params    2
     * @return void
     * @since 下午 3:36 2019/5/29 0029
     **/
    private static void dealRuns(XWPFParagraph paragraph, Map<String, String> params) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String text = run.getText(runs.get(i).getTextPosition());
            if (StringUtils.isNotEmpty(text)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isEmpty(text) || !text.contains(key))
                        continue;
                    key = key.replace("{", "\\{");
                    //判断有没有换行
                    String value = entry.getValue();
                    String[] split = value.split("\r");
                    text = text.replaceAll(key, split[0]);
                    run.setText(text, 0);
                    if (split.length > 1) {
                        //居右
                        paragraph.setAlignment(ParagraphAlignment.RIGHT);
                        for (int j = 1; j < split.length; j++) {
                            //硬回车  enter
                            run.addCarriageReturn();
                            run.setText(split[j]);
                        }
                    }
                }
            }
        }
    }


    /**
     * 读取word内容
     *
     * @param file 1
     * @return java.lang.String
     * @since 下午 1:52 2019/6/25 0025
     **/
    public static String readWord(File file) {

        if (Objects.isNull(file))
            throw new NullPointerException("读取的文件为空");

        String buffer = "";
        if (FileUtils.getFileSuffix(file).toLowerCase().endsWith(".doc")) {
            try (InputStream is = new FileInputStream(file)) {

                HWPFDocument hwpfDocument = new HWPFDocument(is);
                //文档文本内容
                StringBuilder text = hwpfDocument.getText();
                buffer = text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (FileUtils.getFileSuffix(file).toLowerCase().endsWith("docx")) {
            try (InputStream is = new FileInputStream(file)) {
                XWPFDocument xwpfDocument = new XWPFDocument(is);
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(xwpfDocument);
                buffer = xwpfWordExtractor.getText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("读取的文件不是word文件,无法读取");
        }
        return buffer;
    }


//    public static void main(String[] args) {
//        String s = readWord(new File("E:\\公司资料\\公司\\项目评估\\2019-06-24翠微谷草稿.docx"));
//        System.out.println(s);
//        int i = s.indexOf("{{");
//        int i1 = s.indexOf("}}", i + 2);
//        if (i != -1 && i1 != -1) {
//            String substring = s.substring(i + 2, i1);
//            System.out.println(substring);
//        }
//    }

}
