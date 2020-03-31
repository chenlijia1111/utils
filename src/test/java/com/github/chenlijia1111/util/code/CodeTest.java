package com.github.chenlijia1111.util.code;

import com.github.chenlijia1111.utils.code.mybatis.CommonMapperCommentGenerator;
import com.github.chenlijia1111.utils.code.mybatis.MybatisCodeGeneratorUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/28 0028 下午 8:26
 **/
public class CodeTest {


    private static MybatisCodeGeneratorUtil mybatisCodeGeneratorUtil = MybatisCodeGeneratorUtil.getInstance();

    static {

        mybatisCodeGeneratorUtil.setCommentGeneratorType(CommonMapperCommentGenerator.class.getName())
                .setConnectionUrl("jdbc:mysql://cdb-lob0ggj0.bj.tencentcdb.com:10048/socialMall?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8").
                setDriverClass("com.mysql.jdbc.Driver").
                setUserId("root").setPassword("clj123456@")
                .setTargetProjectPath("D:\\java\\projects\\utils\\src\\test\\java").
                setTargetDAOPackage("com.github.chenlijia1111.util.code.dao").setTargetEntityPackage("com.github.chenlijia1111.util.code.entity").
                setTargetXMLPackage("com.github.chenlijia1111.util.code.mapper").
                setTargetControllerPackage("com.github.chenlijia1111.util.code.controller").
                setTargetBizPackage("com.github.chenlijia1111.util.code.biz").
                setTargetServicePackage("com.github.chenlijia1111.util.code.service")
                .setAuthor("chenLiJia");

        mybatisCodeGeneratorUtil.setExampleCode(false);
        mybatisCodeGeneratorUtil.setCommonCode(false);

        Map<String, String> tableToDomain = mybatisCodeGeneratorUtil.getTableToDoMain();
        tableToDomain.put("m_carousel", "Carousel");

        List<String> ignoreDoMainToBusiness = mybatisCodeGeneratorUtil.getIgnoreDoMainToBusiness();

    }


    //生成entity,mapper,dao
    @Test
    public void test1WithChen() {
        mybatisCodeGeneratorUtil.generateCode();
    }

    //生成controller,biz,service
    @Test
    public void test2WithChen() {

        mybatisCodeGeneratorUtil.generateWithBusinssCode();
    }


}
