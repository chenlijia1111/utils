package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.constant.BooleanConstant;
import com.github.chenlijia1111.utils.common.constant.TimeConstant;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.dateTime.DateTimeConvertUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.HttpUtils;
import com.github.chenlijia1111.utils.list.Lists;
import com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField;
import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
 * <p>
 * 可自定义导出暂时没有宽度的设置 {@link ExcelExportField#cellWidth()}
 * <p>
 * 2020-08-10 修改表格样式为只生成一次
 * 优化 row 对象 cell 对象 使用之后就将引用指向 null
 * 方便 gc 回收空间
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 8:41
 **/
public class ExcelExport {

    /**
     * 工作薄对象
     */
    private Workbook workbook;

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
     * 忽略导出的字段
     */
    private List<String> ignoreFieldList;

    /**
     * 导出的文件名称
     * 如果没有设置
     * 默认以当前时间为文件名称
     **/
    private String exportFileName;

    /**
     * 属性->属性信息
     * 减少反复反射查询带来的消耗
     */
    private Map<String, Optional<FieldInfoItem>> fieldInfoMap = new HashMap<>();

    /**
     * 字段信息
     */
    private class FieldInfoItem {

        /**
         * 字段类型
         */
        Class fieldType;

        /**
         * 字段注解
         */
        ExcelExportField excelExportField;
    }


    public ExcelExport setDataList(List<? extends Object> dataList) {
        this.dataList = dataList;
        return this;
    }

    public ExcelExport setTransferMap(Map<String, Function> transferMap) {
        this.transferMap = transferMap;
        return this;
    }

    public ExcelExport setIgnoreFieldList(List<String> ignoreFieldList) {
        this.ignoreFieldList = ignoreFieldList;
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


    /**
     * 默认后缀名为 xlsx
     *
     * @param exportFileName
     * @return
     */
    public ExcelExport setExportFileName(String exportFileName) {
        if (StringUtils.isEmpty(exportFileName)) {
            this.exportFileName = DateTimeConvertUtil.dateToStr(new Date(), TimeConstant.DATE_TIME) + ".xlsx";
        } else if (!FileUtils.checkFileSuffix(exportFileName, "xls", "xlsx")) {
            //非法文件名 加默认后缀
            this.exportFileName = exportFileName + ".xlsx";
        } else {
            //合法后缀名
            this.exportFileName = exportFileName;
        }
        return this;
    }

    public Map<String, Function> getTransferMap() {
        if (Objects.isNull(transferMap)) {
            transferMap = new HashMap<>();
        }
        return transferMap;
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


        //导出
        try (ServletOutputStream outputStream = response.getOutputStream();) {

            //设置响应头
            response.setContentType("application/vnd.ms-excel");
            this.exportFileName = URLDecoder.decode(this.exportFileName, "UTF-8");
            if (HttpUtils.isIE(request)) {
                this.exportFileName = URLEncoder.encode(this.exportFileName, "UTF-8");
            } else {
                this.exportFileName = new String(this.exportFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + this.exportFileName);

            //执行 重载方法 导出
            exportData(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化数据
     * 校验 exportFieldList 如果没有手动设置要导出的字段，则取对象里的导出属性
     *
     * @return void
     * @since 下午 9:08 2019/9/3 0003
     **/
    public void exportData(OutputStream outputStream) {

        if (this.exportFileName.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            //判断数据量，如果数据量大于500条，使用 SXSSFWorkbook 进行导出
            if (this.dataList.size() >= 1000) {
                workbook = new SXSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook();
            }
        }

        sheet = workbook.createSheet();

        //可以导出空数据,但是对象导出对象以及集合不能为空 否则无法确定表头
        AssertUtil.isTrue(null != dataList, "导出的数据集合为null");
        AssertUtil.isTrue(null != exportClass, "导出的数据Class 对象为null");
        AssertUtil.isTrue(null != outputStream, "输出流为空");

        //初始化表头数据
        initHeadTitle();

        //初始化表格数据
        checkData();

        //校验是否设置了导出文件名
        if (StringUtils.isEmpty(this.exportFileName)) {
            //设置默认名城
            this.exportFileName = DateTimeConvertUtil.dateToStr(new Date(), TimeConstant.DATE_TIME);
        }

        //导出
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }

            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理数据
     * <p>
     * 把要导出的数据放到表格中
     *
     * @return void
     * @since 上午 11:05 2019/9/4 0004
     **/
    private void checkData() {

        List<?> dataList = this.dataList;

        AssertUtil.isTrue(null != exportClass, "导出的数据Class 对象为null");

        //表格样式
        CellStyle cellStyle = simpleCellStyle(workbook, true, false);
        //表头样式
        CellStyle headCellStyle = simpleCellStyle(workbook, true, true);

        //导出表头
        LinkedHashMap<String, String> exportTitleHeadNameMap = this.exportTitleHeadNameMap;
        Row headRow = sheet.createRow(0);
        //第一列 序号
        Cell serialCell = headRow.createCell(0);
        serialCell.setCellValue("序号");
        serialCell.setCellStyle(cellStyle);
        int currentHeadIndex = 1;

        Set<Map.Entry<String, String>> entries = exportTitleHeadNameMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        //列对象引用，不要放在循环内，防止对象引用太多，影响垃圾回收
        Cell cell;
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String headName = next.getValue();
            cell = headRow.createCell(currentHeadIndex);
            cell.setCellValue(headName);
            cell.setCellStyle(headCellStyle);

            currentHeadIndex++;
        }

        int size = dataList.size();
        if (size > 0) {

            //行对象引用
            Row row;
            //第一列 序号 引用
            Cell serialCellValue;
            //转换器 引用
            Function function;
            // 插入图片对象
            HSSFPatriarch drawingPatriarch = null;

            for (int i = 0; i < size; i++) {

                int rowIndex = i + 1;
                row = sheet.createRow(rowIndex);
                //第一列 序号
                serialCellValue = row.createCell(0);
                serialCellValue.setCellValue(i + 1);
                serialCellValue.setCellStyle(cellStyle);

                Object o = dataList.get(i);
                //获取对象的所有属性
                //当前列下标
                int currentColumnIndex = 1;
                for (String fieldName : exportTitleHeadNameMap.keySet()) {

                    try {
                        Object fieldValue = PropertyUtil.getFieldValue(o, exportClass, fieldName);
                        // 查询对应的注解
                        ExcelExportField excelExportField = findFieldInfo(fieldName).map(e -> e.excelExportField).orElse(null);
                        if (null != fieldValue) {
                            //判断有没有转换器
                            function = finFieldConvert(fieldName);
                            if (Objects.nonNull(function)) {
                                //有转换器
                                Object apply = function.apply(fieldValue);
                                cell = row.createCell(currentColumnIndex);
                                cell.setCellValue(apply.toString());
                                cell.setCellStyle(cellStyle);
                            } else if (Objects.nonNull(excelExportField) && Objects.equals(BooleanConstant.YES_INTEGER, excelExportField.imageStatus())) {
                                // 该字段表示的是图片
                                // 判断字段是否是字符串
                                Class fileType = findFieldInfo(fieldName).map(e -> e.fieldType).orElse(null);
                                if (Objects.equals(String.class, fileType)) {
                                    // 是字符串
                                    // 判断是否是网络图片--不推荐网络图片，因为如果有大量网络图片的话，
                                    // 后台导出需要将所有网络图片都请求一遍，将会消耗很多时间，可以考虑让前端进行导出
                                    String imagePath = fieldValue.toString();
                                    byte[] bytes = null;
                                    if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                                        // 是网络图片
                                        HttpResponse response = HttpClientUtils.getInstance().doGet(imagePath).toResponse();
                                        if (Objects.nonNull(response)) {
                                            InputStream inputStream = response.getEntity().getContent();
                                            if (Objects.nonNull(inputStream)) {
                                                // 获取字节数组
                                                bytes = IOUtil.readToBytes(inputStream);
                                            }
                                        }
                                    } else {
                                        // 是本地图片
                                        File file = new File(imagePath);
                                        if (file.exists() && !file.isDirectory()) {
                                            FileInputStream inputStream = new FileInputStream(file);
                                            // 读取图片文件内容
                                            bytes = IOUtil.readToBytes(inputStream);
                                        }
                                    }
                                    // 如果对应的图片数据存在的话，就放入图片数据，构建图片
                                    if (Objects.nonNull(bytes) && bytes.length > 0) {
                                        if (Objects.isNull(drawingPatriarch)) {
                                            drawingPatriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
                                        }

                                        HSSFClientAnchor hssfClientAnchor = new HSSFClientAnchor(0, 0, 255, 255,
                                                (short) currentColumnIndex, rowIndex, (short) currentColumnIndex, rowIndex);
                                        int pictureIndex = workbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
                                        drawingPatriarch.createPicture(hssfClientAnchor, pictureIndex);
                                    }
                                }
                            } else {
                                cell = row.createCell(currentColumnIndex);
                                cell.setCellValue(fieldValue.toString());
                                cell.setCellStyle(cellStyle);
                            }
                        }

                    } catch (NoSuchFieldException e) {
                        //如果没有这个属性,那就是自定义的属性，需要单独处理
                        //判断有没有转换器,直接对传进来的对象进行转换操作
                        function = finFieldConvert(fieldName);
                        if (Objects.nonNull(function)) {
                            //有转换器
                            Object apply = function.apply(o);
                            cell = row.createCell(currentColumnIndex);
                            cell.setCellValue(apply.toString());
                            cell.setCellStyle(cellStyle);
                        } else {
                            cell = row.createCell(currentColumnIndex);
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    currentColumnIndex++;

                }
            }
        }
    }

    /**
     * 获取字段的转换器
     *
     * @param fieldName
     * @return
     */
    private Function finFieldConvert(String fieldName) {

        Function function = null;

        if (this.getTransferMap().containsKey(fieldName)) {
            return this.getTransferMap().get(fieldName);
        }

        if (StringUtils.isNotEmpty(fieldName)) {
            //先判断有没有在 transferMap 定义转换器
            if (Objects.nonNull(transferMap)) {
                function = transferMap.get(fieldName);
            }
            //如果没有，再去注解中寻找有没有定义转换器
            if (Objects.isNull(function)) {
                try {
                    ExcelExportField excelExportField = findFieldInfo(fieldName).map(e -> e.excelExportField).orElse(null);
                    if (Objects.nonNull(excelExportField)) {
                        Class<? extends Function> convert = excelExportField.convert();
                        if (!Objects.equals(convert, ExcelExportField.NoConvert.class)) {
                            function = convert.newInstance();
                        }
                    }
                } catch (IllegalAccessException e) {
                } catch (InstantiationException e) {
                }
            }
        }


        if (Objects.nonNull(function) && !this.getTransferMap().containsKey(fieldName)) {
            this.getTransferMap().put(fieldName, function);
        }
        return function;
    }

    /**
     * 获取字段的信息
     *
     * @param fieldName
     * @return
     */
    private Optional<FieldInfoItem> findFieldInfo(String fieldName) {

        Optional<FieldInfoItem> optional = null;
        // 先查询在 fieldInfoMap 里有没有存过，有的话，就直接返回了
        if (fieldInfoMap.containsKey(fieldName)) {
            optional = fieldInfoMap.get(fieldName);
        } else {
            // 通过反射去获取
            try {
                Field field = this.exportClass.getDeclaredField(fieldName);
                if (Objects.nonNull(field)) {
                    FieldInfoItem fieldInfoItem = new FieldInfoItem();
                    Class<?> type = field.getType();
                    fieldInfoItem.fieldType = type;
                    ExcelExportField excelExportField = field.getAnnotation(ExcelExportField.class);
                    if (Objects.nonNull(excelExportField)) {
                        fieldInfoItem.excelExportField = excelExportField;
                    }
                    optional = Optional.ofNullable(fieldInfoItem);
                    fieldInfoMap.put(fieldName, optional);
                }
            } catch (NoSuchFieldException e) {
            }
        }

        return optional;
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
                    String fieldName = field.getName();
                    //判断是否要忽略这个字段
                    if (Lists.isNotEmpty(ignoreFieldList) && ignoreFieldList.contains(fieldName)) {
                        continue;
                    }
                    ExcelExportField annotation = findFieldInfo(fieldName).map(e -> e.excelExportField).orElse(null);
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
     * @param workbook     1
     * @param borderStatus 是否要边框
     * @param boldStatus   是否加粗
     * @return void
     * @since 上午 10:07 2019/9/4 0004
     **/
    public static CellStyle simpleCellStyle(Workbook workbook, boolean borderStatus, boolean boldStatus) {

        //初始样式
        CellStyle style = workbook.createCellStyle();

        //设置样式  上下左右边框 字体 居中 宽度
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        if (borderStatus) {
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        }

        Font dataFont = workbook.createFont();
        dataFont.setFontName("宋体");
        dataFont.setFontHeightInPoints((short) 10);
        if (boldStatus) {
            // 加粗
            dataFont.setBold(true);
        }
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
