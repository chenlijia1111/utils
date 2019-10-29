package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Objects;

/**
 * excel工具类
 *
 * @author 陈礼佳
 * @since 2019/9/10 22:03
 */
public class ExcelUtil {

    /**
     * 获取 workBook 对象
     *
     * @param excelFile
     * @return
     */
    public static Workbook getWorkBook(File excelFile) {
        //校验参数
        AssertUtil.isTrue(Objects.nonNull(excelFile), "excel文件为空");
        AssertUtil.isTrue(excelFile.exists(), "excel文件不存在");

        //获取后缀
        String fileSuffix = FileUtils.getFileSuffix(excelFile);
        AssertUtil.isTrue(Lists.asList(".xls", ".xlsx").contains(fileSuffix), "文件格式不合法");

        Workbook workbook = null;
        try (FileInputStream inputStream = new FileInputStream(excelFile)) {
            if (Objects.equals(".xls", fileSuffix)) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    /**
     * 获取单元格的内容
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 将excel输出到文件
     * @param workBook
     * @param outPutFile
     */
    public static void writeToFile(Workbook workBook, File outPutFile) {
        try (FileOutputStream outputStream = new FileOutputStream(outPutFile)) {
            workBook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
