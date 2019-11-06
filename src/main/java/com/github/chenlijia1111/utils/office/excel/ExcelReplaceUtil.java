package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * excel替换工具类
 *
 * @author 陈礼佳
 * @since 2019/9/10 21:53
 */
public class ExcelReplaceUtil {


    /**
     * 替换 excel内容
     * 先是创建一个临时文件用于存储将要生成的文件内容
     * 最后把临时文件的数据输出到输出文件上去
     * 这样可以防止输入文件与输出文件是同一个文件是替换失败的情况
     * <p>
     * 文件中需要替换的内容用 {} 进行包裹起来 实例：{key}
     *
     * @param map        替换的键值对映射  示例: key -> value
     * @param inputFile  输入文件
     * @param outPutFile 输出文件
     */
    public static void doReplace(Map<String, String> map, File inputFile, File outPutFile) {

        //校验参数
        AssertUtil.isTrue(Objects.nonNull(inputFile), "输入文件不能为空");
        AssertUtil.isTrue(Objects.nonNull(outPutFile), "输出文件不能为空");
        AssertUtil.isTrue(inputFile.exists(), "输入文件不存在");

        //判断是否是excel文件类型
        List<String> excelTypeList = Lists.asList(".xls", ".xlsx");
        AssertUtil.isTrue(excelTypeList.contains(FileUtils.getFileSuffix(inputFile).toLowerCase()), "文件类型不是excel的文件类型");

        if (map != null && map.size() > 0) {
            //开始处理
            //创建一个临时文件
            String tempFileName = RandomUtil.createUUID() + FileUtils.getFileSuffix(inputFile);
            String parent = inputFile.getParent();
            File tempFile = new File(parent + "/" + tempFileName);
            try {
                tempFile.createNewFile();
                Workbook workBook = ExcelUtil.getWorkBook(inputFile);
                //获取所有的sheet
                for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                    Sheet sheet = workBook.getSheetAt(i);
                    //获取当前sheet的开始行
                    int firstRowNum = sheet.getFirstRowNum();
                    //获取当前sheet的结束行
                    int lastRowNum = sheet.getLastRowNum();

                    for (int j = firstRowNum; j < lastRowNum; j++) {
                        Row row = sheet.getRow(j);
                        //获得当前行的开始列
                        int firstCellNum = row.getFirstCellNum();
                        //获得当前行的列数
                        int lastCellNum = row.getPhysicalNumberOfCells();
                        //循环当前行
                        if (-1 == firstCellNum) {
                            continue;
                        }
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            String cellValue = ExcelUtil.getCellValue(cell);
                            if (StringUtils.isNotEmpty(cellValue)) {
                                //判断是否有需要进行替换的内容
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();
                                    if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)
                                            && cellValue.contains("{" + key + "}")) {
                                        //进行替换 替换使用的是正则 需要使用转义符
                                        cellValue = cellValue.replaceAll("\\{" + key + "}", value);
                                    }
                                }
                                cell.setCellValue(cellValue);
                            }
                        }
                    }

                }

                //复制到临时文件里去
                ExcelUtil.writeToFile(workBook, tempFile);
                //然后再把临时文件的内容复制到输出文件里面去
                FileUtils.copyFile(tempFile, outPutFile);

                //最后删除临时文件
                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
