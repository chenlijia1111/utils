package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.list.Lists;
import com.github.chenlijia1111.utils.office.excel.annos.ExcelImportField;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel 导入工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/11 0011 下午 5:41
 **/
public class ExcelImportUtil {

    /**
     * 读取excel
     *
     * @param file          处理的excel文件
     * @param tClass        转换的类
     * @param startRowIndex 开始处理的行
     * @return java.util.List<T>
     * @since 下午 5:49 2019/10/11 0011
     **/
    public static <T> List<T> importExcel(File file, Class<T> tClass, int startRowIndex) {
        //先判断文件是否合法
        AssertUtil.isTrue(null != file && file.exists() && FileUtils.checkFileSuffix(file.getName(), "xls", "xlsx"), "文件不合法");

        //开始处理
        try (InputStream inputStream = new FileInputStream(file)) {
            return importExcel(inputStream, tClass, startRowIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取excel
     * 请在调用此方法前判断好输入流是excel文件的输入流
     *
     * @param inputStream   处理的输入流
     * @param tClass        转换的类
     * @param startRowIndex 开始处理的行
     * @return java.util.List<T>
     * @since 下午 5:49 2019/10/11 0011
     **/
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> tClass, int startRowIndex) {
        //先判断文件是否合法
        AssertUtil.isTrue(null != inputStream, "输入流为空");

        //开始处理
        try {
            List<T> list = new ArrayList<T>();

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            List<Field> allFields = PropertyUtil.getAllFields(tClass);
            if (lastRowNum >= startRowIndex) {
                for (int i = startRowIndex; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);

                    T instance = tClass.newInstance();

                    //处理有注解的属性
                    if (Lists.isNotEmpty(allFields)) {
                        for (Field allField : allFields) {
                            Class<?> type = allField.getType();
                            ExcelImportField excelImportField = allField.getAnnotation(ExcelImportField.class);
                            if (null != excelImportField) {
                                int cellIndex = excelImportField.cellIndex();
                                Cell cell = row.getCell(cellIndex);
                                if (null != cell) {
                                    //进行转换,判断需要转换的类型,这里只支持基本类型和String、Date
                                    Object o = cellValueToTargetClass(cell, type);
                                    //开始给属性赋值
                                    allField.setAccessible(true);
                                    allField.set(instance, o);
                                }
                            }
                        }
                    }

                    list.add(instance);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 表格内容转换为java对象值
     *
     * @param cell        1
     * @param targetClass 2
     * @return T
     * @since 下午 6:29 2019/10/11 0011
     **/
    private static <T> T cellValueToTargetClass(Cell cell, Class<T> targetClass) {
        if (null != cell) {
            if (String.class == targetClass) {
                return (T) ExcelUtil.getCellValue(cell);
            } else if (Byte.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isInt(cellValue)) {
                    return (T) Byte.valueOf(cellValue);
                }
            } else if (Short.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isInt(cellValue)) {
                    return (T) Short.valueOf(cellValue);
                }
            } else if (Integer.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isInt(cellValue)) {
                    return (T) Integer.valueOf(cellValue);
                }
            } else if (Long.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isInt(cellValue)) {
                    return (T) Long.valueOf(cellValue);
                }
            } else if (BigInteger.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isInt(cellValue)) {
                    return (T) BigInteger.valueOf(Long.valueOf(cellValue));
                }
            } else if (Float.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isDouble(cellValue)) {
                    return (T) Float.valueOf(cellValue);
                }
            } else if (Double.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isDouble(cellValue)) {
                    return (T) Double.valueOf(cellValue);
                }
            } else if (BigDecimal.class == targetClass) {
                String cellValue = ExcelUtil.getCellValue(cell);
                if (StringUtils.isDouble(cellValue)) {
                    return (T) new BigDecimal(cellValue);
                }
            } else if (Date.class == targetClass) {
                Date dateCellValue = cell.getDateCellValue();
                if (null != dateCellValue) {
                    return (T) dateCellValue;
                }
            }
        }
        return null;
    }

}
