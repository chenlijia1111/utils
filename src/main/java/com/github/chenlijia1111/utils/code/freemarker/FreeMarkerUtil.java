package com.github.chenlijia1111.utils.code.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * freeMarker代码生成工具类
 *
 * freeMarker 如果在模板中引用的数据为空就会报空指针异常，所以要做好非空判断
 * <#if user??>
 *
 * </#if>
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/3 0003 上午 9:27
 **/
public class FreeMarkerUtil {


    /**
     * 输出模板文件
     *
     * @param templateFile 模板文件
     * @param outputFile   输出文件
     * @param params       参数
     * @return void
     * @since 上午 9:35 2019/10/3 0003
     **/
    public static void writeTemplateToFile(File templateFile, String outputFile, Map<String, Object> params) {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        //输出流
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))) {
            //设置模板路径
            configuration.setDirectoryForTemplateLoading(templateFile.getParentFile());
            //获取模板文件
            Template template = configuration.getTemplate(templateFile.getName());
            //输出文件
            template.process(params, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
