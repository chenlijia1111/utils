package com.github.chenlijia1111.utils.code.mybatis;

import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * mybatis 代码生成之注释生成器之通用mapper
 * 通用mapper需要搭配 jpa注解
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/7/6 0006 下午 2:04
 **/
public class CommonMapperCommentGenerator extends DefaultCommentGenerator {


    private Properties properties;
    private Properties systemPro;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private String currentDateStr;

    public CommonMapperCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }


    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString().replace("\n", " "));
        field.addJavaDocLine(" */");
        //添加swagger注解
        field.addAnnotation("@ApiModelProperty(\"" + introspectedColumn.getRemarks() + "\")");
        field.addAnnotation("@PropertyCheck(name = \"" + introspectedColumn.getRemarks() + "\")");
        //主键
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        if (Lists.isNotEmpty(primaryKeyColumns) &&
                primaryKeyColumns.stream().map(e -> e.getActualColumnName()).collect(Collectors.toSet()).contains(introspectedColumn.getActualColumnName())) {
            field.addAnnotation("@Id");
        }
        //Column注解
        field.addAnnotation("@Column(name = \"" + introspectedColumn.getActualColumnName() + "\")");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        //导包
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        //属性校验注解
        topLevelClass.addImportedType("com.github.chenlijia1111.utils.core.annos.PropertyCheck");
        //jpa
        topLevelClass.addImportedType("javax.persistence.*");

        //添加swagger注解
        topLevelClass.addAnnotation("@ApiModel(\"" + introspectedTable.getRemarks() + "\")");
        //table注解
        topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\")");

        StringBuilder sb = new StringBuilder();
        sb.append("/**\n");
        sb.append(" * " + introspectedTable.getRemarks() + "\n");
        sb.append(" * @author " + (StringUtils.isNotEmpty(MybatisCodeGeneratorUtil.author) ? MybatisCodeGeneratorUtil.author : "chenLiJia") + "\n");
        sb.append(" * @since " + fetchCurrentTimeStr() + "\n");
        sb.append(" * @version 1.0\n");
        sb.append(" **/");
        topLevelClass.addJavaDocLine(sb.toString());
    }


    /**
     * 获取当前时间字符串
     *
     * @return java.lang.String
     * @author chenlijia
     * @Description TODO
     * @Date 上午 10:54 2019/7/6 0006
     **/
    private String fetchCurrentTimeStr() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
