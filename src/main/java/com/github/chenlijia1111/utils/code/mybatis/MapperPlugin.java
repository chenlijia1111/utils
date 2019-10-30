package com.github.chenlijia1111.utils.code.mybatis;

import com.github.chenlijia1111.utils.core.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.plugins.MapperAnnotationPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mapper类处理
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/30 0030 下午 7:41
 **/
public class MapperPlugin extends MapperAnnotationPlugin {


    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //取entity的路径
        String targetEntityPackage = MybatisCodeGeneratorUtil.getInstance().getTargetEntityPackage();
        //截取路径,开始处理
        String shortName = interfaze.getType().getShortName();//AnnounceMapper  mapper类名
        //实体名称
        String entityName = shortName.substring(0, shortName.indexOf("Mapper"));

        //导包
        interfaze.addImportedType(new FullyQualifiedJavaType(targetEntityPackage + "." + entityName));
        interfaze.addImportedType(new FullyQualifiedJavaType("tk.mybatis.mapper.common.Mapper"));

        //加父类
        interfaze.addSuperInterface(new FullyQualifiedJavaType("tk.mybatis.mapper.common.Mapper<" + entityName + ">"));

        //加注解
        StringBuilder sb = new StringBuilder();
        sb.append("/**\n");
        sb.append(" * " + introspectedTable.getRemarks() + "\n");
        sb.append(" * @author " + (StringUtils.isNotEmpty(MybatisCodeGeneratorUtil.author) ? MybatisCodeGeneratorUtil.author : "chenLiJia") + "\n");
        sb.append(" * @since " + fetchCurrentTimeStr() + "\n");
        sb.append(" * @version 1.0\n");
        sb.append(" **/");

        interfaze.addJavaDocLine(sb.toString());
        return true;
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
