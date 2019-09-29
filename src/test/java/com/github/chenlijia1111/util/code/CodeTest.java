package com.github.chenlijia1111.util.code;

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


    private static MybatisCodeGeneratorUtil mybatisCodeGeneratorUtil = new MybatisCodeGeneratorUtil();

    static {
        mybatisCodeGeneratorUtil.setCommentGeneratorType("com.github.chenlijia1111.utils.code.mybatis.CommentGenerator")
                .setConnectionUrl("jdbc:mysql://rm-wz9yfa169ro1w5vbtdo.mysql.rds.aliyuncs.com:3306/ji9u?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8").
                setDriverClass("com.mysql.jdbc.Driver").
                setUserId("ji9u").setPassword("Ji9yousite")
                .setTargetProjectPath("C:\\Users\\Administrator\\mycode\\utils\\src\\test\\java").
                setTargetDAOPackage("com.github.chenlijia1111.util.code.dao").
                setTargetEntityPackage("com.github.chenlijia1111.util.code.entity").
                setTargetXMLPackage("com.github.chenlijia1111.util.code.mapper").
                setTargetControllerPackage("com.github.chenlijia1111.util.code.controller").
                setTargetBizPackage("com.github.chenlijia1111.util.code.biz").
                setTargetServicePackage("com.github.chenlijia1111.util.code.service")
                .setAuthor("chenLiJia");

        Map<String, String> tableToDomain = mybatisCodeGeneratorUtil.getTableToDoMain();
        tableToDomain.put("j_group_image", "GroupImage");

        List<String> ignoreDoMainToBusiness = mybatisCodeGeneratorUtil.getIgnoreDoMainToBusiness();
//        ignoreDoMainToBusiness.add("FightGroupItem");
//        ignoreDoMainToBusiness.add("FightGroupItemClient");
//        ignoreDoMainToBusiness.add("ShopFightGroup");
    }


    //生成entity,mapper,dao
    @Test
    public void test1WithChen() {
        mybatisCodeGeneratorUtil.setExampleCode(true);
        mybatisCodeGeneratorUtil.generateCode();
    }

    //生成controller,biz,service
    @Test
    public void test2WithChen() {
        mybatisCodeGeneratorUtil.generateWithBusinssCode();
    }

}
