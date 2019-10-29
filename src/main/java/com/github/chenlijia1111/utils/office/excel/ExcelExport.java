package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.constant.TimeConstant;
import com.github.chenlijia1111.utils.core.PropertyCheckUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.http.HttpUtils;
import com.github.chenlijia1111.utils.list.Lists;
import com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;

/**
 * 导出 excel 工具类
 * <p>
 * <p>
 * 如果是使用注解进行导出 只需要设置导出名称与 导出数据即可
 * <p>
 * 如果是使用自定义方式进行导出 需要设置导出的头以及对应的字段名称
 * 示例代码:
 * LinkedHashMap<String, String> headTitle = new LinkedHashMap<>();
 * headTitle.put("groupId", "订单编号");
 * headTitle.put("createTime", "订单时间");
 * 如果碰到两个表头对应同一个属性值的情况，可以通过设置转换特性的属性名以及转换方法来进行实现
 * {@link #transferMap} 转换方法 {@link #exportTitleHeadNameMap}
 * 当属性不存在时，会默认以对象的数据来进行转换
 *
 * 自定义导出暂时没有宽度的设置,后期可以加
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 8:41
 **/
public class ExcelExport {

    /**
     * 工作薄对象
     */
    private HSSFWorkbook workbook;

    /**
     * 工作表对象
     */
    private Sheet sheet;


    /**
     * 导出数据
     **/
    private List<? extends Object> dataList;

    /**
     * 导出的class类型
     * 构造方法传入
     *
     * @since 上午 11:24 2019/9/4 0004
     **/
    private Class<?> exportClass;

    /**
     * 数据转换方法
     * 在类定义中往往有很多在数据库总是以整形标记数据类型，但是，在渲染显示的时候又需要转化为特定的内容来显示
     * 所以在这里定义 transferMap 来对需要转化的属性进行转化
     * key 为属性名称 value 为具体的转换方法
     **/
    private Map<String, Function> transferMap;

    /**
     * 导出的表头名称
     * 如果没有设置
     * 默认取导出字段中设置的名称
     * <p>
     * 按顺序进行排序
     * <p>
     * key  属性名称 value 表头名称
     **/
    private LinkedHashMap<String, String> exportTitleHeadNameMap;


    /**
     * 导出的字段数组
     * 支持自定义导出列
     * 也支持 指定注解导出
     * 暂时没有使用到 可以忽略
     *
     * @see #exportTitleHeadNameMap
     **/
    private List<String> exportFieldList;

    /**
     * 导出的文件名称
     * 如果没有设置
     * 默认以当前时间为文件名称
     **/
    private String exportFileName;


    public ExcelExport setDataList(List<? extends Object> dataList) {
        this.dataList = dataList;
        return this;
    }

    public ExcelExport setTransferMap(Map<String, Function> transferMap) {
        this.transferMap = transferMap;
        return this;
    }

    /**
     * 如果设置了要导出的字段 则直接用设置的导出的字段
     * 否则 取对象里有 {@link com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField} 注解的属性
     *
     * @param exportFieldList 1
     * @return com.github.chenlijia1111.utils.office.excel.ExcelExport
     * @since 下午 9:03 2019/9/3 0003
     **/
    public ExcelExport setExportFieldList(List<String> exportFieldList) {
        this.exportFieldList = exportFieldList;
        return this;
    }

    public ExcelExport setExportTitleHeadNameMap(LinkedHashMap<String, String> exportTitleHeadNameMap) {
        this.exportTitleHeadNameMap = exportTitleHeadNameMap;
        return this;
    }


    public ExcelExport setExportFileName(String exportFileName) {
        if (StringUtils.isEmpty(exportFileName)) {
            this.exportFileName = DateTime.now().toString(TimeConstant.DATE) + ".xls";
        } else if (!(exportFileName.toLowerCase().endsWith(".xls")
                || exportFileName.toLowerCase().endsWith(".xlsx"))) {
            this.exportFileName = exportFileName + ".xls";
        } else {
            this.exportFileName = exportFileName;
        }
        return this;
    }

    /**
     * 构造方法
     *
     * @param exportFileName 导出文件名称
     * @param exportClass    导出的
     * @return
     * @since 下午 5:05 2019/9/4 0004
     **/
    public ExcelExport(String exportFileName, Class exportClass) {
        setExportFileName(exportFileName);
        this.exportClass = exportClass;
    }

    /**
     * 初始化数据
     * 校验 exportFieldList 如果没有手动设置要导出的字段，则取对象里的导出属性
     *
     * @return void
     * @since 下午 9:08 2019/9/3 0003
     **/
    public void exportData(HttpServletRequest request, HttpServletResponse response) {

        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();

        //可以导出空数据,但是对象导出对象以及集合不能为空 否则无法确定表头
        AssertUtil.isTrue(null != dataList, "导出的数据集合为null");
        AssertUtil.isTrue(null != exportClass, "导出的数据Class 对象为null");
        AssertUtil.isTrue(null != response, "response对象为空");

        //初始化表头数据
        initHeadTitle();

        //初始化表格数据
        checkData();

        //校验是否设置了导出文件名
        if (StringUtils.isEmpty(this.exportFileName)) {
            //设置默认名城
            this.exportFileName = DateTime.now().toString(TimeConstant.DATE_TIME);
        }

        //导出
        try (ServletOutputStream outputStream = response.getOutputStream();) {

            response.setContentType("application/vnd.ms-excel");
            this.exportFileName = URLDecoder.decode(this.exportFileName, "UTF-8");
            if (HttpUtils.isIE(request)) {
                this.exportFileName = URLEncoder.encode(this.exportFileName, "UTF-8");
            } else {
                this.exportFileName = new String(this.exportFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + this.exportFileName);

            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理数据
     * <p>
     * 把要到处的数据放到表格中
     *
     * @return void
     * @since 上午 11:05 2019/9/4 0004
     **/
    private void checkData() {

        List<?> dataList = this.dataList;

        AssertUtil.isTrue(null != exportClass, "导出的数据Class 对象为null");

        //导出表头
        LinkedHashMap<String, String> exportTitleHeadNameMap = this.exportTitleHeadNameMap;
        Row headRow = sheet.createRow(0);
        //第一列 序号
        Cell serialCell = headRow.createCell(0);
        serialCell.setCellValue("序号");
        serialCell.setCellStyle(simpleCellStyle(workbook));
        int currentHeadIndex = 1;

        Set<Map.Entry<String, String>> entries = exportTitleHeadNameMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String headName = next.getValue();
            Cell cell = headRow.createCell(currentHeadIndex);
            cell.setCellValue(headName);
            cell.setCellStyle(simpleCellStyle(workbook));

            currentHeadIndex++;
        }


        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                //第一列 序号
                Row row = sheet.createRow(i + 1);
                Cell serialCellValue = row.createCell(0);
                serialCellValue.setCellValue(i + 1);
                serialCellValue.setCellStyle(simpleCellStyle(workbook));

                Object o = dataList.get(i);
                //获取对象的所有属性
                //当前列下标
                int currentColumnIndex = 1;
                for (String fieldName : exportTitleHeadNameMap.keySet()) {
                    try {
                        Object fieldValue = PropertyUtil.getFieldValue(o, exportClass, fieldName);
                        if (null != fieldValue) {
                            //判断有没有转换器
                            if (null != transferMap && transferMap.get(fieldName) != null) {
                                //有转换器
                                Function function = transferMap.get(fieldName);
                                Object apply = function.apply(fieldValue);
                                Cell cell = row.createCell(currentColumnIndex);
                                cell.setCellValue(apply.toString());
                                cell.setCellStyle(simpleCellStyle(workbook));
                            } else {
                                Cell cell = row.createCell(currentColumnIndex);
                                cell.setCellValue(fieldValue.toString());
                                cell.setCellStyle(simpleCellStyle(workbook));
                            }
                        }


                    } catch (NoSuchFieldException e) {
                        //如果没有这个属性,那就是自定义的属性，需要单独处理
                        //判断有没有转换器
                        //判断有没有转换器,直接对对象进行转换操作
                        if (null != transferMap && transferMap.get(fieldName) != null) {
                            //有转换器
                            Function function = transferMap.get(fieldName);
                            Object apply = function.apply(o);
                            Cell cell = row.createCell(currentColumnIndex);
                            cell.setCellValue(apply.toString());
                            cell.setCellStyle(simpleCellStyle(workbook));
                        } else {
                            Cell cell = row.createCell(currentColumnIndex);
                            cell.setCellValue("");
                            cell.setCellStyle(simpleCellStyle(workbook));
                        }
                    }

                    currentColumnIndex++;
                }
            }
        }

    }


    /**
     * 表头名称
     * 通过自定义 或者传入的 class对象进行解析
     * 如果是注解方式 初始化表头宽度
     *
     * @return void
     * @since 下午 12:57 2019/9/4 0004
     **/
    private void initHeadTitle() {
        //处理表头
        AssertUtil.isTrue(null != exportClass, "导出的数据Class 对象为null");

        //如果自定义了就不初始化了
        if (null == exportTitleHeadNameMap || exportTitleHeadNameMap.size() == 0) {
            Class<?> exportClass = this.exportClass;
            List<Field> allFields = PropertyUtil.getAllFields(exportClass);
            //判断有ExcelExportField 注解的属性
            if (Lists.isNotEmpty(allFields)) {
                //以sort进行排序
                ArrayList<ExcelExportHeadInfo> headInfoArrayList = new ArrayList<>();
                for (int i = 0; i < allFields.size(); i++) {
                    Field field = allFields.get(i);
                    ExcelExportField annotation = field.getAnnotation(ExcelExportField.class);
                    if (null != annotation) {
                        String titleHeadName = annotation.titleHeadName();
                        ExcelExportHeadInfo headInfo = new ExcelExportHeadInfo(field.getName(), titleHeadName, annotation.sort(), annotation.cellWidth());
                        headInfoArrayList.add(headInfo);
                    }
                }

                if (headInfoArrayList.size() == 0) {
                    throw new IllegalArgumentException("没有到导出的字段");
                } else {
                    //赋值导出的表头
                    LinkedHashMap<String, String> titleHeadHashMap = new LinkedHashMap<>();
                    //以 sort进行排序
                    Collections.sort(headInfoArrayList, Comparator.comparing(ExcelExportHeadInfo::getSort));
                    for (int i = 0; i < headInfoArrayList.size(); i++) {
                        ExcelExportHeadInfo headInfo = headInfoArrayList.get(i);
                        titleHeadHashMap.put(headInfo.getFieldName(), headInfo.getHeadName());
                        //设置表格列宽度  宽度公式
                        sheet.setColumnWidth(i + 1, 256 * headInfo.getCellWidth() + 184);
                    }
                    this.exportTitleHeadNameMap = titleHeadHashMap;
                }

            } else {
                throw new IllegalArgumentException("没有要导出的字段");
            }

        }
    }

    /**
     * 初始化样式
     *
     * @param workbook 1
     * @return void
     * @since 上午 10:07 2019/9/4 0004
     **/
    private CellStyle simpleCellStyle(Workbook workbook) {

        //初始样式
        CellStyle style = workbook.createCellStyle();

        //设置样式  上下左右边框 字体 居中 宽度
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        Font dataFont = workbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);

        //水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //第一个参数代表列id(从0开始),第2个参数代表宽度值  参考 ："2012-08-10"的宽度为2500
        //设置自动换行:
        style.setWrapText(true);//设置自动换行

        return style;
    }


}
