package com.github.chenlijia1111.utils.code.mybatis;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import io.swagger.annotations.ApiModel;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * mybatis 代码生成工具类
 * <p>
 * 如果不想生成controller代码 可以把 {@link #targetControllerPackage} 设为空
 * 同理 service biz 也是如此
 * <p>
 * 可以通过设置基础包名来设置每个模块的包名，每个模块都有缺省包名
 * {@link #targetBasePackage}
 * {@link #targetControllerPackage}
 * {@link #targetBizPackage}
 * {@link #targetServicePackage}
 * {@link #targetXMLPackage}
 * {@link #targetDAOPackage}
 * {@link #targetEntityPackage}
 * <p>
 * <p>
 * 关于生成的Mapper
 * 通用Mapper默认只继承了 {@link tk.mybatis.mapper.common.Mapper} 接口，这个接口拥有了基本的增删改查的功能
 * 如果想要增加一些通用功能，可以直接继承一些已经写好的Mapper接口，如下：
 * {@link tk.mybatis.mapper.additional.idlist.IdListMapper} 对于id批量操作的Mapper，
 * 根据id集合批量以及批量删除
 * {@link tk.mybatis.mapper.common.special.InsertListMapper} 批量插入，但是要求必须是主键自增的，
 * 且id的字段名必须是id
 * {@link tk.mybatis.mapper.additional.insert.InsertListMapper} 批量插入，主键需要自己设置好，不自动生成主键
 * <p>
 * 通用Mapper中Example相关的方法都挺好用的，基本上单表查询有他就不用自己写sql了，用法如下：
 * {@code
 * public void testQueryExample(){
 * Example example = Example.builder(Carousel.class).andWhere(Sqls.custom().
 * andEqualTo("name", "名字1")).build();
 * carouselMapper.selectByExample(example);
 * }
 * }
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/7/6 0006 下午 2:03
 **/
public class MybatisCodeGeneratorUtil {

    private static Logger logger = new LogUtil(MybatisCodeGeneratorUtil.class);

    /**
     * 生成实体代码注释的 generator 实现
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:16 2019/7/6 0006
     **/
    private String commentGeneratorType;

    /**
     * 数据库连接地址
     * jdbc:mysql://58.250.17.31:33306/expertise?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
     * <p>
     * 注意 &useUnicode=true&characterEncoding=utf-8 必须带上，不然可能会出现乱码的现象
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:26 2019/7/6 0006
     **/
    private String connectionUrl;

    /**
     * 数据库驱动
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String driverClass = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库用户名
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String userId;

    /**
     * 数据库密码
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String password;


    /**
     * 生成代码对应的顶级目录
     * D:\ssmProject\waibao\parent\web\src\main\java
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetProjectPath;

    /**
     * 包名前缀
     * 如 com.chenlijia
     * 设置了前缀之后，如果没有设置其他包对应的包名，就是使用默认值进行拼接
     * 否则使用设置的其他包名进行拼接
     */
    private String targetBasePackage;

    /**
     * 对应的 dao 的包名地址  com.chenlijia.dao
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetDAOPackage = "dao";

    /**
     * 对应的 mapper 的包名地址  com.chenlijia.mapper
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetXMLPackage = "mapper";


    /**
     * 对应的 entity 的包名地址  com.chenlijia.entity
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetEntityPackage = "entity";


    /**
     * 对应的 controller 的包名地址  com.chenlijia.controller
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetControllerPackage = "controller";


    /**
     * 对应的 biz 的包名地址  com.chenlijia.biz
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetBizPackage = "biz";


    /**
     * 对应的 service 的包名地址  com.chenlijia.service
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:27 2019/7/6 0006
     **/
    private String targetServicePackage = "service";


    /**
     * 生成的代码的作者
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:01 2019/7/17 0017
     **/
    public static String author;

    /**
     * 是否生成示例代码  这个还是挺有用的，查询使用
     *
     * @since 下午 8:41 2019/9/28 0028
     **/
    private boolean exampleCode = false;

    /**
     * 是否生成通用的方法 如增删改查
     * 如果集成了通用mapper 就不需要这个了
     *
     * @see CommonMapperCommentGenerator
     */
    private boolean commonCode = true;


    /**
     * 数据库表名 以及对应的实体名称
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 2:29 2019/7/6 0006
     **/
    private Map<String, String> tableToDoMain = new HashMap<>();


    /**
     * 需要忽略生成业务代码的实体
     *
     * @author chenlijia
     * @Description TODO
     * @Date 下午 4:06 2019/7/6 0006
     **/
    private List<String> ignoreDoMainToBusiness = new ArrayList<>();

    public static MybatisCodeGeneratorUtil instance = new MybatisCodeGeneratorUtil();

    private MybatisCodeGeneratorUtil() {
    }

    /**
     * 单例
     *
     * @return com.github.chenlijia1111.utils.code.mybatis.MybatisCodeGeneratorUtil
     * @since 下午 7:51 2019/10/30 0030
     **/
    public static MybatisCodeGeneratorUtil getInstance() {
        return instance;
    }

    public String getCommentGeneratorType() {
        return commentGeneratorType;
    }

    public MybatisCodeGeneratorUtil setCommentGeneratorType(String commentGeneratorType) {
        this.commentGeneratorType = commentGeneratorType;
        return this;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * 这里可是被坑惨了
     * {@link com.mysql.cj.jdbc.DatabaseMetaDataUsingInfoSchema#getColumnPrivileges(String, String, String, String)}
     * {@link com.mysql.cj.jdbc.DatabaseMetaData#getDatabase(String, String)} 根据这个参数 nullDatabaseMeansCurrent 来决定返回的数据库是不是null
     *
     * @param connectionUrl
     * @return
     */
    public MybatisCodeGeneratorUtil setConnectionUrl(String connectionUrl) {
        //处理url，这里可真实被坑惨了
        if (connectionUrl.contains("?")) {
            connectionUrl = connectionUrl + "&nullCatalogMeansCurrent=true";
        } else {
            connectionUrl = connectionUrl + "?nullCatalogMeansCurrent=true";
        }
        this.connectionUrl = connectionUrl;
        return this;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public MybatisCodeGeneratorUtil setDriverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public MybatisCodeGeneratorUtil setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MybatisCodeGeneratorUtil setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getTargetProjectPath() {
        return targetProjectPath;
    }

    public MybatisCodeGeneratorUtil setTargetProjectPath(String targetProjectPath) {
        this.targetProjectPath = targetProjectPath;
        return this;
    }

    public String getTargetDAOPackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetDAOPackage;
        }
        return targetDAOPackage;
    }

    public MybatisCodeGeneratorUtil setTargetDAOPackage(String targetDAOPackage) {
        this.targetDAOPackage = targetDAOPackage;
        return this;
    }

    public String getTargetXMLPackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetXMLPackage;
        }
        return targetXMLPackage;
    }

    public MybatisCodeGeneratorUtil setTargetXMLPackage(String targetXMLPackage) {
        this.targetXMLPackage = targetXMLPackage;
        return this;
    }

    public String getTargetEntityPackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetEntityPackage;
        }
        return targetEntityPackage;
    }

    public MybatisCodeGeneratorUtil setTargetEntityPackage(String targetEntityPackage) {
        this.targetEntityPackage = targetEntityPackage;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public MybatisCodeGeneratorUtil setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Map<String, String> getTableToDoMain() {
        return tableToDoMain;
    }


    public List<String> getIgnoreDoMainToBusiness() {
        return ignoreDoMainToBusiness;
    }

    public String getTargetControllerPackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetControllerPackage;
        }
        return targetControllerPackage;
    }

    public MybatisCodeGeneratorUtil setTargetControllerPackage(String targetControllerPackage) {
        this.targetControllerPackage = targetControllerPackage;
        return this;
    }

    public String getTargetBizPackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetBizPackage;
        }
        return targetBizPackage;
    }

    public MybatisCodeGeneratorUtil setTargetBizPackage(String targetBizPackage) {
        this.targetBizPackage = targetBizPackage;
        return this;
    }

    public String getTargetServicePackage() {
        if (StringUtils.isNotEmpty(this.targetBasePackage)) {
            return this.targetBasePackage + "." + targetServicePackage;
        }
        return targetServicePackage;
    }

    public MybatisCodeGeneratorUtil setTargetServicePackage(String targetServicePackage) {
        this.targetServicePackage = targetServicePackage;
        return this;
    }

    public MybatisCodeGeneratorUtil setExampleCode(boolean exampleCode) {
        this.exampleCode = exampleCode;
        return this;
    }

    public MybatisCodeGeneratorUtil setCommonCode(boolean commonCode) {
        this.commonCode = commonCode;
        return this;
    }

    public MybatisCodeGeneratorUtil setTargetBasePackage(String targetBasePackage) {
        this.targetBasePackage = targetBasePackage;
        return this;
    }

    public void generateCode() {

        //检验参数是否正确
        AssertUtil.hasText(connectionUrl, "数据库连接地址为空");
        AssertUtil.hasText(userId, "数据库用户名为空");
//        Assert.hasText(password,"数据库密码为空");
        AssertUtil.hasText(targetProjectPath, "生成代码对应的顶级目录为空");
        AssertUtil.hasText(getTargetDAOPackage(), "对应的 dao 的包名地址为空");
        AssertUtil.hasText(getTargetXMLPackage(), "对应的 mapper 的包名地址为空");
        AssertUtil.hasText(getTargetEntityPackage(), "对应的 entity 的包名地址为空");

        AssertUtil.isTrue(Objects.nonNull(tableToDoMain) && tableToDoMain.size() > 0, "数据库表名 以及对应的实体名称映射为空");


        List<String> warnings = new ArrayList<>();
        boolean overWrite = true;
        Configuration config = new Configuration();
        Context context = new Context(ModelType.CONDITIONAL);
        context.setId("context1");
        context.setTargetRuntime("MyBatis3");

        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.addProperty("suppressDate", "true");
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
        commentGeneratorConfiguration.addProperty("javaFileEncoding", "UTF-8");
        if (StringUtils.isNotEmpty(this.getCommentGeneratorType())) {
            commentGeneratorConfiguration.setConfigurationType(this.getCommentGeneratorType());
        }
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        //如果是生成通用Mapper,使用自定义Mapper插件修改Mapper继承基类
        if (Objects.equals(commentGeneratorConfiguration.getConfigurationType(), CommonMapperCommentGenerator.class.getName())) {
            //添加自定义Mapper生成插件
            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType(MapperPlugin.class.getName());
            context.addPluginConfiguration(pluginConfiguration);
        }

        //数据库连接配置
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(this.connectionUrl);
        jdbcConnectionConfiguration.setDriverClass(this.driverClass);
        jdbcConnectionConfiguration.setUserId(this.userId);
        jdbcConnectionConfiguration.setPassword(this.password);
        //生成dao代码 ...ByPrimaryKey的方法
        jdbcConnectionConfiguration.addProperty("useInformationSchema", "true");
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        //生成模型设置
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage(getTargetEntityPackage());
        javaModelGeneratorConfiguration.setTargetProject(this.targetProjectPath);
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        //mapper
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage(getTargetXMLPackage());
        sqlMapGeneratorConfiguration.setTargetProject(this.targetProjectPath);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        //dao mapper
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(getTargetDAOPackage());
        javaClientGeneratorConfiguration.setTargetProject(this.targetProjectPath);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        Set<Map.Entry<String, String>> entries = tableToDoMain.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String tableName = entry.getKey();
            String domainClassName = entry.getValue();
            //生成表设置
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(tableName);
            tableConfiguration.setDomainObjectName(domainClassName);
            //默认不生成Example的代码
            tableConfiguration.setCountByExampleStatementEnabled(exampleCode);
            tableConfiguration.setUpdateByExampleStatementEnabled(exampleCode);
            tableConfiguration.setDeleteByExampleStatementEnabled(exampleCode);
            tableConfiguration.setSelectByExampleStatementEnabled(exampleCode);
            tableConfiguration.setUpdateByPrimaryKeyStatementEnabled(commonCode);
            tableConfiguration.setDeleteByPrimaryKeyStatementEnabled(commonCode);
            tableConfiguration.setInsertStatementEnabled(commonCode);
            //不管怎样,都留一个逐渐查询的方法,不然不生成xml以及resultMap
            tableConfiguration.setSelectByPrimaryKeyStatementEnabled(true);

            context.addTableConfiguration(tableConfiguration);
        }

        config.addContext(context);

        DefaultShellCallback callback = new DefaultShellCallback(overWrite);
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 业务代码代码生成
     * controller
     * biz
     * service
     *
     * @return void
     * @author chenlijia
     * @Description TODO
     * @Date 下午 1:03 2019/7/1 0001
     **/
    public void generateWithBusinessCode() {

        if (Objects.nonNull(this.tableToDoMain) && this.tableToDoMain.size() > 0) {

            //要生成的业务层代码的实体类
            ArrayList<Class> classes = new ArrayList<>();
            for (Map.Entry<String, String> stringStringEntry : tableToDoMain.entrySet()) {
                String value = stringStringEntry.getValue();
                try {
                    //判断是否需要忽略该实体
                    if (ignoreDoMainToBusiness.contains(value)) {
                        continue;
                    }
                    Class<?> aClass = Class.forName(getTargetEntityPackage() + "." + value);
                    classes.add(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            Class[] entityClassArray = classes.toArray(new Class[classes.size()]);

            if (StringUtils.isEmpty(targetProjectPath))
                throw new RuntimeException("当前项目代码存放路径为空");

            if (StringUtils.isNotEmpty(targetControllerPackage)) {
                generateController(entityClassArray);
            }

            if (StringUtils.isNotEmpty(targetBizPackage)) {
                generateBiz(entityClassArray);
            }

            if (StringUtils.isNotEmpty(targetServicePackage)) {
                generateService(entityClassArray);
            }
        }
    }


    /**
     * 生成biz代码
     *
     * @return void
     * @author chenlijia
     * @Description TODO
     * @Date 上午 11:35 2019/7/1 0001
     **/
    private void generateBiz(Class[] entityClassArray) {


        String absoluteBizPackagePath = StringUtils.isNotEmpty(getTargetBizPackage()) ? getTargetBizPackage().replaceAll("\\.", "/") : "";
        //biz绝对路径
        absoluteBizPackagePath = StringUtils.isNotEmpty(absoluteBizPackagePath) ? (targetProjectPath + "/" + absoluteBizPackagePath) : targetProjectPath;

        if (entityClassArray != null && entityClassArray.length > 0) {
            int length = entityClassArray.length;
            for (int i = 0; i < length; i++) {
                Class entityClass1 = entityClassArray[i];
                //实体注释
                String comment = getEntityApiModelComment(entityClass1);
                String entityClassName = entityClass1.getSimpleName();
                //校验文件夹是否存在,不存在则创建
                FileUtils.checkDirectory(absoluteBizPackagePath);
                File controllerFile = new File(absoluteBizPackagePath + "/" + entityClassName + "Biz.java");
                try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(controllerFile))) {
                    //controller包路径
                    printWriter.println("package " + getTargetBizPackage() + ";");
                    printWriter.println();
                    //导包
                    printWriter.println("import org.springframework.stereotype.Service;");
                    printWriter.println();
                    //注释
                    printComment(printWriter, comment);
                    //注解
                    printWriter.println("@Service");
                    printWriter.println("public class " + entityClassName + "Biz {");
                    printWriter.println();
                    printWriter.println("}");
                    printWriter.flush();
                    logger.info("输出biz: " + controllerFile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 生成service代码
     *
     * @return void
     * @author chenlijia
     * @Description TODO
     * @Date 上午 11:35 2019/7/1 0001
     **/
    private void generateService(Class[] entityClassArray) {


        String absoluteServicePackagePath = StringUtils.isNotEmpty(getTargetServicePackage()) ? getTargetServicePackage().replaceAll("\\.", "/") : "";
        //控制器绝对路径
        absoluteServicePackagePath = StringUtils.isNotEmpty(absoluteServicePackagePath) ? (targetProjectPath + "/" + absoluteServicePackagePath) : targetProjectPath;

        if (entityClassArray != null && entityClassArray.length > 0) {
            int length = entityClassArray.length;
            for (int i = 0; i < length; i++) {
                Class entityClass1 = entityClassArray[i];
                //实体注释
                String comment = getEntityApiModelComment(entityClass1);
                //实体的名字(类名)
                String entityClassName = entityClass1.getSimpleName();
                //完整名(包含包)
                String name = entityClass1.getName();
                //校验文件夹是否存在,不存在则创建
                FileUtils.checkDirectory(absoluteServicePackagePath);
                File serviceFile = new File(absoluteServicePackagePath + "/" + entityClassName + "ServiceI.java");
                File serviceImplFile = new File(absoluteServicePackagePath + "/impl/" + entityClassName + "ServiceImpl.java");
                //生成service 文件
                generateServiceCode(serviceFile, comment, entityClassName, name);
                //生成impl文件
                generateServiceImplCode(serviceImplFile, comment, entityClassName, name);
            }
        }

    }


    private void generateServiceCode(File serviceFile, String comment, String entityClassName, String entityFullName) {

        //防止文件夹不存在
        FileUtils.checkDirectory(serviceFile.getParent());

        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(serviceFile))) {
            //controller包路径
            printWriter.println("package " + getTargetServicePackage() + ";");
            printWriter.println();
            //导入Result
            printWriter.println("import com.github.chenlijia1111.utils.common.Result;");
            printWriter.println("import java.util.List;");
            //导入Example
            printWriter.println("import tk.mybatis.mapper.entity.Example;");
            //导入实体
            printWriter.println("import " + entityFullName + ";");
            printWriter.println();
            //注释
            printComment(printWriter, comment);
            printWriter.println("public interface " + entityClassName + "ServiceI {");
            printWriter.println();
            //增加方法
            //方法注释
            printWriter.println("    /**\n" +
                    "     * 添加\n" +
                    "     *\n" +
                    "     * @param params      1\n" +
                    "     * @return com.github.chenlijia1111.utils.common.Result\n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    Result add(" + entityClassName + " params);");
            printWriter.println();

            //编辑方法
            //方法注释
            printWriter.println("" +
                    "    /**\n" +
                    "     * 编辑\n" +
                    "     *\n" +
                    "     * @param params      1\n" +
                    "     * @return com.github.chenlijia1111.utils.common.Result\n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    Result update(" + entityClassName + " params);");
            printWriter.println();

            //编辑方法 按条件编辑
            //方法注释
            printWriter.println(
                    "   /**\n" +
                    "     * 按条件编辑\n" +
                    "     * @param entity\n" +
                    "     * @param condition\n" +
                    "     * @return\n" +
                    "     */");
            //方法
            printWriter.println("    Result update(" + entityClassName + " entity,Example condition);");
            printWriter.println();

            //条件查询方法
            printWriter.println("" +
                    "    /**\n" +
                    "     * 条件查询\n" +
                    "     *\n" +
                    "     * @param condition      1\n" +
                    "     * @return \n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    List<" + entityClassName + "> listByCondition(" + entityClassName + " condition);");
            printWriter.println();

            //example 条件查询方法
            printWriter.println("" +
                    "    /**\n" +
                    "     * 条件查询\n" +
                    "     *\n" +
                    "     * @param condition      1\n" +
                    "     * @return \n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    List<" + entityClassName + "> listByCondition(Example condition);");
            printWriter.println();

            printWriter.println();
            printWriter.println("}");
            printWriter.flush();
            logger.info("输出service: " + serviceFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void generateServiceImplCode(File serviceImplFile, String comment, String entityClassName, String entityFullName) {

        //防止文件夹不存在
        FileUtils.checkDirectory(serviceImplFile.getParent());

        //生成impl
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(serviceImplFile))) {
            //serviceImpl包路径
            printWriter.println("package " + getTargetServicePackage() + ".impl;");
            printWriter.println();
            //导包
            printWriter.println("import com.github.chenlijia1111.utils.common.Result;");//Result
            printWriter.println("import com.github.chenlijia1111.utils.core.PropertyCheckUtil;");//参数校验
            //导入Example
            printWriter.println("import tk.mybatis.mapper.entity.Example;");

            printWriter.println("import " + entityFullName + ";");//实体
            printWriter.println("import " + getTargetDAOPackage() + "." + entityClassName + "Mapper;");//dao 接口
            printWriter.println("import " + getTargetServicePackage() + "." + entityClassName + "ServiceI;");//service接口
            printWriter.println("import org.springframework.stereotype.Service;");//@Service接口
            printWriter.println();
            printWriter.println("import javax.annotation.Resource;");//Resource注解
            printWriter.println("import java.util.List;");
            printWriter.println("import java.util.ArrayList;");
            printWriter.println("import java.util.Objects;");
            printWriter.println();
            //注释
            printComment(printWriter, comment);
            //注解
            printWriter.println("@Service");
            printWriter.println("public class " + entityClassName + "ServiceImpl implements " + entityClassName + "ServiceI {");
            printWriter.println();
            printWriter.println();


            //注入dao
            printWriter.println("    @Resource");
            String daoName = entityClassName.substring(0, 1).toLowerCase() + entityClassName.substring(1) + "Mapper";
            printWriter.println("    private " + entityClassName + "Mapper " + daoName + ";");
            printWriter.println();
            printWriter.println();


            //增加方法
            //方法注释
            printWriter.println("" +
                    "    /**\n" +
                    "     * 添加\n" +
                    "     *\n" +
                    "     * @param params      添加参数\n" +
                    "     * @return com.github.chenlijia1111.utils.common.Result\n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    @Override");
            printWriter.println("    public Result add(" + entityClassName + " params){");
            printWriter.println("    ");
            printWriter.println("        int i = " + daoName + ".insertSelective(params);");
            printWriter.println("        return i > 0 ? Result.success(\"操作成功\") : Result.failure(\"操作失败\");");
            printWriter.println("    }");
            printWriter.println();

            //编辑方法
            //方法注释
            printWriter.println("" +
                    "    /**\n" +
                    "     * 编辑\n" +
                    "     *\n" +
                    "     * @param params      编辑参数\n" +
                    "     * @return com.github.chenlijia1111.utils.common.Result\n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    @Override");
            printWriter.println("    public Result update(" + entityClassName + " params){");
            printWriter.println("    ");
            printWriter.println("        int i = " + daoName + ".updateByPrimaryKeySelective(params);");
            printWriter.println("        return i > 0 ? Result.success(\"操作成功\") : Result.failure(\"操作失败\");");
            printWriter.println("    }");
            printWriter.println();

            //编辑 按条件编辑
            //方法注释
            printWriter.println(
                    "    /**\n" +
                    "     * 按条件编辑\n" +
                    "     * @param entity\n" +
                    "     * @param condition\n" +
                    "     * @return\n" +
                    "     */");
            //方法
            printWriter.println(
                    "    @Override\n" +
                    "    public Result update(" + entityClassName + " entity, Example condition) {\n" +
                    "        if(Objects.nonNull(entity) && Objects.nonNull(condition)){\n" +
                    "            int i = " + daoName + ".updateByExampleSelective(entity, condition);\n" +
                    "            return i > 0 ? Result.success(\"操作成功\") : Result.failure(\"操作失败\");\n" +
                    "        }\n" +
                    "        return Result.success(\"操作成功\");\n" +
                    "    }");
            printWriter.println();


            //条件查询方法
            //方法注释
            printWriter.println("" +
                    "    /**\n" +
                    "     * 条件查询\n" +
                    "     *\n" +
                    "     * @param condition  \n" +
                    "     * @return \n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    @Override");
            printWriter.println("    public List<" + entityClassName + "> listByCondition(" + entityClassName + " condition){");
            printWriter.println("    ");
            printWriter.println("        PropertyCheckUtil.transferObjectNotNull(condition, true);");
            printWriter.println("        return " + daoName + ".select(condition);");
            printWriter.println("    }");
            printWriter.println();

            //example条件查询方法
            //方法注释
            printWriter.println("" +
                    "    /**\n" +
                    "     * 条件查询\n" +
                    "     *\n" +
                    "     * @param condition  \n" +
                    "     * @return \n" +
                    "     * @since " + fetchCurrentTimeStr() + "\n" +
                    "     **/");
            //方法
            printWriter.println("    @Override");
            printWriter.println("    public List<" + entityClassName + "> listByCondition(Example condition){");
            printWriter.println("    ");
            printWriter.println("        if (null != condition) {");
            printWriter.println("           List<" + entityClassName + "> list = " + daoName + ".selectByExample(condition);");
            printWriter.println("           return list;");
            printWriter.println("        }");
            printWriter.println("        return new ArrayList<>();");
            printWriter.println("    }");
            printWriter.println();

            printWriter.println();
            printWriter.println("}");
            printWriter.flush();
            logger.info("输出serviceImpl: " + serviceImplFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成controller代码
     *
     * @return void
     * @author chenlijia
     * @Description TODO
     * @Date 上午 11:35 2019/7/1 0001
     **/
    private void generateController(Class[] entityClassArray) {

        String absoluteControllerPackagePath = StringUtils.isNotEmpty(getTargetControllerPackage()) ? getTargetControllerPackage().replaceAll("\\.", "/") : "";
        //控制器绝对路径
        absoluteControllerPackagePath = StringUtils.isNotEmpty(absoluteControllerPackagePath) ? (targetProjectPath + "/" + absoluteControllerPackagePath) : targetProjectPath;

        if (entityClassArray != null && entityClassArray.length > 0) {
            int length = entityClassArray.length;
            for (int i = 0; i < length; i++) {
                Class entityClass1 = entityClassArray[i];
                //实体注释
                String comment = getEntityApiModelComment(entityClass1);
                String entityClassName = entityClass1.getSimpleName();
                //校验文件夹是否存在,不存在则创建
                FileUtils.checkDirectory(absoluteControllerPackagePath);
                File controllerFile = new File(absoluteControllerPackagePath + "/" + entityClassName + "Controller.java");
                try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(controllerFile))) {
                    //controller包路径
                    printWriter.println("package " + getTargetControllerPackage() + ";");
                    printWriter.println();
                    //导包
                    printWriter.println("import org.springframework.web.bind.annotation.RestController;");
                    printWriter.println();
                    //注释
                    printComment(printWriter, comment);
                    printWriter.println("@RestController");
                    printWriter.println("public class " + entityClassName + "Controller {");
                    printWriter.println();
                    printWriter.println("}");
                    printWriter.flush();
                    logger.info("输出controller: " + controllerFile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 输出注释
     *
     * @param printWriter 1
     * @param comment     注释内容
     * @return void
     * @author chenlijia
     * @Description TODO
     * @Date 上午 10:56 2019/7/6 0006
     **/
    private void printComment(PrintWriter printWriter, String comment) {
        printWriter.println("/**");
        if (StringUtils.isNotEmpty(comment))
            printWriter.println(" * " + comment);
        printWriter.println(" * @author " + (StringUtils.isNotEmpty(author) ? author : "chenLiJia"));
        printWriter.println(" * @since " + fetchCurrentTimeStr());
        printWriter.println(" **/");
    }


    /**
     * 获取实体中的注解
     *
     * @param entityClass 1
     * @return java.lang.String
     * @author chenlijia
     * @Description TODO
     * @Date 上午 11:08 2019/7/6 0006
     **/
    public String getEntityApiModelComment(Class entityClass) {
        if (Objects.nonNull(entityClass)) {
            ApiModel apiModel = (ApiModel) entityClass.getDeclaredAnnotation(ApiModel.class);
            if (Objects.nonNull(apiModel)) {
                String value = apiModel.value();
                if (StringUtils.isNotEmpty(value)) {
                    return value;
                }
            }
        }
        return null;
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
