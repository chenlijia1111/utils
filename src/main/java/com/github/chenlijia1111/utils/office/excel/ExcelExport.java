package com.github.chenlijia1111.utils.office.excel;

import com.github.chenlijia1111.utils.list.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 导出 excel 工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 8:41
 **/
public class ExcelExport {


    /**
     * 工作薄对象
     */
    private HSSFWorkbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;


    /**
     * 导出数据
     **/
    private List<? extends Object> dataList;

    /**
     * 数据转换方法
     * 在类定义中往往有很多在数据库总是以整形标记数据类型，但是，在渲染显示的时候又需要转化为特定的内容来显示
     * 所以在这里定义 transferMap 来对需要转化的属性进行转化
     * key 为属性名称 value 为具体的转换方法
     **/
    private Map<String, Function> transferMap;

    /**
     * 导出的字段数组
     **/
    private List<String> exportFieldList;


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

    /**
     * 校验 exportFieldList 如果没有手动设置要导出的字段，则取对象里的导出属性
     *
     * @return void
     * @since 下午 9:08 2019/9/3 0003
     **/
    private void checkExportFieldList() {
        if (Lists.isEmpty(exportFieldList)) {

        }
    }

    private void export() {

    }


}
