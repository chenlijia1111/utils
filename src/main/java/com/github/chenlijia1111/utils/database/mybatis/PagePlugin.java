package com.github.chenlijia1111.utils.database.mybatis;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.database.mybatis.pojo.Page;
import com.github.chenlijia1111.utils.database.mybatis.pojo.PageThreadLocalParameter;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * mybatis 分页插件
 * <p>
 * 可以拦截的类型 Executor ParameterHandler ResultSetHandler StatementHandler
 * 可以拦截的方法
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 * <p>
 * 当前就是拦截 Executor 的 query 重定义sql
 * args 定义的参数 mybatis 会注入到 {@link Invocation#getArgs()}
 * <p>
 * 使用方式
 * 首先将 {@link PagePlugin} 注入mybatis 的插件中 在springBoot 直接用 @Bean 注入spring容器即可
 * 然后在查询列表之前使用
 * Page.startPage(page,limit); 注入线程变量 {@link Page}
 * 然后执行查询 List<Product> products = productMapper.listAllProduct();
 * 然后消费线程变量 {@link com.github.chenlijia1111.utils.database.mybatis.pojo.PageInfo}
 * PageInfo<Product> pageInfo = new PageInfo<>(products);
 *
 *
 * 参考PageOffice进行实现
 *
 * @author Chen LiJia
 * @see com.github.chenlijia1111.utils.database.mybatis.pojo.PageInfo 分页对象
 * @since 2019/12/25
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class PagePlugin implements Interceptor {

    //分页统计id后缀
    private static final String PAGE_COUNT_ID_SUFFIX = "page_count_id_suffix";

    //分页id后缀
    private static final String PAGE_ID_SUFFIX = "page_id_suffix";

    //page 字段名
    private static final String PAGE_FIELD_NAME = "page";

    //limit 字段名
    private static final String LIMIT_FIELD_NAME = "limit";

    /**
     * 自动分页
     * 如果设置为true,在没有找到线程变量的时候,会自动从参数中查找
     * page 参数和 limit 参数来进行自当分页
     * 也就是不用显示调用 Page.startPage(page,limit);
     * 只要在参数中传入 page 和 limit 参数即可
     *
     * 如果参数中存在 page limit 参数，但是又不想自动分页，只需要把 其中一个置为空即可
     */
    private boolean autoPage = false;


    /**
     * 执行拦截逻辑的方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        //注入的参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];

        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = mappedStatement.getBoundSql(parameterObject);
            cacheKey = executor.createCacheKey(mappedStatement, parameterObject, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        //获取分页参数
        Page page = findCurrentPageParameter(parameterObject);
        if (Objects.nonNull(page)) {
            //进行分页操作
            //创建一个MappedStatement
            String countMsId = mappedStatement.getId() + PAGE_COUNT_ID_SUFFIX;
            MappedStatement countMappedStatement = MSUtils.newCountMappedStatement(mappedStatement, countMsId);
            Long autoCount = ExecutorUtil.executeAutoCount(executor, countMappedStatement, parameterObject, boundSql, rowBounds, resultHandler);
            page.setCount(autoCount.intValue());
            //重新赋值线程变量，更新里面的 count 值
            PageThreadLocalParameter.setPageParameter(page);

            //设置新的MappedStatement 加上分页
            List resultList = ExecutorUtil.pageQuery(executor,
                    mappedStatement, parameterObject, rowBounds, resultHandler, boundSql, cacheKey);
            return resultList;
        }

        return invocation.proceed();
    }

    /**
     * 查找线程变量
     * 如果没有
     * 判断是否需要自动分页
     * 如果是--从参数中获取分页参数
     * 注入线程变量中
     *
     * @param parameterObject 请求参数
     * @return
     */
    private Page findCurrentPageParameter(Object parameterObject) {
        Page page = PageThreadLocalParameter.getPageParameter();
        if (Objects.isNull(page) && this.autoPage && Objects.nonNull(parameterObject)) {
            //查询参数中的分页参数
            if (parameterObject instanceof Map) {
                //参数是map  用@Params 注解注入的也是以map的形式传参的
                Map parameterMap = (Map) parameterObject;
                if (parameterMap.containsKey(PAGE_FIELD_NAME) && parameterMap.containsKey(LIMIT_FIELD_NAME)) {
                    Object pageObject = parameterMap.get(PAGE_FIELD_NAME);
                    Object limitObject = parameterMap.get(LIMIT_FIELD_NAME);
                    if (Objects.nonNull(pageObject) && Objects.nonNull(limitObject) && pageObject instanceof Integer && limitObject instanceof Integer) {
                        page = Page.startPage((Integer) pageObject, (Integer) limitObject);
                    }
                }
            } else {
                //查找分页字段
                try {
                    Object pageObject = PropertyUtil.getFieldValue(parameterObject, parameterObject.getClass(), PAGE_FIELD_NAME);
                    Object limitObject = PropertyUtil.getFieldValue(parameterObject, parameterObject.getClass(), LIMIT_FIELD_NAME);
                    if (Objects.nonNull(pageObject) && Objects.nonNull(limitObject) && pageObject instanceof Integer && limitObject instanceof Integer) {
                        page = Page.startPage((Integer) pageObject, (Integer) limitObject);
                    }
                } catch (NoSuchFieldException e) {
                    //没有这个属性,不用分页
                }
            }
        } else {
            //如果page 不为空的话,需要判断是否已经分过一次页了,
            //如果前面已经分过一次页了,那么后面的查询即使没有被消费掉,也不应该再次分页了,
            //这样消费线程变量参数就不需要紧跟查询了
            if (Objects.nonNull(page) && Objects.nonNull(page.getCount())) {
                return null;
            }
        }
        return page;
    }


    /**
     * 生成代理类
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 根据配置来初始化 Interceptor 的方法
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 创建统计数量的MappedStatement
     *
     * @param ms
     * @param parameterObject
     * @return
     */
    private MappedStatement createCountMappedStatement(MappedStatement ms, Object parameterObject) {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String sql = boundSql.getSql();

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("SELECT COUNT(*) FROM (");
        sqlStringBuilder.append(sql);
        sqlStringBuilder.append(") temp");
        StaticSqlSource staticSqlSource = new StaticSqlSource(ms.getConfiguration(), sqlStringBuilder.toString(), boundSql.getParameterMappings());


        MappedStatement mappedStatement = newMappedStatement(ms, staticSqlSource, PAGE_COUNT_ID_SUFFIX, parameterObject);
        return mappedStatement;
    }

    /**
     * 创建查询列表的创建统计数量的MappedStatement
     *
     * @param ms
     * @param parameterObject
     * @return
     */
    private MappedStatement createPageMappedStatement(MappedStatement ms, Object parameterObject) {

        //取线程变量
        Page pageParameter = PageThreadLocalParameter.getPageParameter();
        AssertUtil.isTrue(Objects.nonNull(pageParameter), "未启用分页");

        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String sql = boundSql.getSql();

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append(sql);
        sqlStringBuilder.append(" LIMIT ");
        sqlStringBuilder.append(pageParameter.getOffset());
        sqlStringBuilder.append(",");
        sqlStringBuilder.append(pageParameter.getLimit());

        StaticSqlSource staticSqlSource = new StaticSqlSource(ms.getConfiguration(), sqlStringBuilder.toString(), boundSql.getParameterMappings());

        MappedStatement mappedStatement = newMappedStatement(ms, staticSqlSource, PAGE_ID_SUFFIX, parameterObject);

        return mappedStatement;
    }


    /**
     * 创建 MappedStatement
     *
     * @param ms
     * @param sqlSource
     * @param suffix          查询id后缀 通过这个判断是统计的还是查询列表的
     * @param parameterObject 参数
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource sqlSource, String suffix, Object parameterObject) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),
                sqlSource, ms.getSqlCommandType());

        if (ms.getKeyColumns() != null && ms.getKeyColumns().length != 0) {
            StringBuilder keyColumns = new StringBuilder();
            for (String keyColumn : ms.getKeyColumns()) {
                keyColumns.append(keyColumn).append(",");
            }
            keyColumns.delete(keyColumns.length() - 1, keyColumns.length());
            builder.keyColumn(keyColumns.toString());
        }

        builder.keyGenerator(ms.getKeyGenerator());

        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.lang(ms.getLang());
        builder.resource(ms.getResource());
        builder.parameterMap(ms.getParameterMap());

        if (Objects.equals(PAGE_COUNT_ID_SUFFIX, suffix)) {
            //是统计的
            ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId() + suffix, int.class, Lists.newArrayList()).build();
            builder.resultMaps(Lists.asList(resultMap));
        } else {
            //是查询列表的,不用变
            builder.resultMaps(ms.getResultMaps());
        }

        builder.resultOrdered(ms.isResultOrdered());
        builder.resultSetType(ms.getResultSetType());
        builder.timeout(ms.getTimeout());
        builder.statementType(ms.getStatementType());
        builder.useCache(ms.isUseCache());
        builder.cache(ms.getCache());
        builder.databaseId(ms.getDatabaseId());
        builder.fetchSize(ms.getFetchSize());
        builder.flushCacheRequired(ms.isFlushCacheRequired());

        if (Objects.nonNull(parameterObject) && parameterObject instanceof ParameterMap) {
            ParameterMap parameterMap = (ParameterMap) parameterObject;
            builder.parameterMap(parameterMap);
        }
        return builder.build();
    }

    public void setAutoPage(boolean autoPage) {
        this.autoPage = autoPage;
    }
}
