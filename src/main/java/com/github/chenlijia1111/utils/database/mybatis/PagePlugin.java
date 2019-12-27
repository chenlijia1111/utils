package com.github.chenlijia1111.utils.database.mybatis;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.database.mybatis.pojo.Page;
import com.github.chenlijia1111.utils.database.mybatis.pojo.PageThreadLocalParameter;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
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
 * @author Chen LiJia
 * @see com.github.chenlijia1111.utils.database.mybatis.pojo.PageInfo 分页对象
 * @since 2019/12/25
 */
@Intercepts(value = {@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PagePlugin implements Interceptor {

    //分页统计id后缀
    public static final String PAGE_COUNT_ID_SUFFIX = "page_count_id_suffix";

    //分页id后缀
    public static final String PAGE_ID_SUFFIX = "page_id_suffix";


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

        //获取分页参数
        Page page = PageThreadLocalParameter.getPageParameter();
        AssertUtil.isTrue(Objects.nonNull(page), "分页参数不存在");

        //构造统计 MappedStatement
        args[0] = createCountMappedStatement(mappedStatement, parameterObject);
        List<Integer> proceed = (List<Integer>) invocation.proceed();
        Integer count = proceed.get(0);
        page.setCount(count);
        //重新赋值线程变量
        PageThreadLocalParameter.setPageParameter(page);

        //设置新的MappedStatement 加上分页
        args[0] = createPageMappedStatement(mappedStatement, parameterObject);
        return invocation.proceed();
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
        StaticSqlSource staticSqlSource = new StaticSqlSource(ms.getConfiguration(), sqlStringBuilder.toString());

        MappedStatement mappedStatement = newMappedStatement(ms, staticSqlSource, PAGE_COUNT_ID_SUFFIX);
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

        StaticSqlSource staticSqlSource = new StaticSqlSource(ms.getConfiguration(), sqlStringBuilder.toString());

        MappedStatement mappedStatement = newMappedStatement(ms, staticSqlSource, PAGE_ID_SUFFIX);
        return mappedStatement;
    }


    /**
     * 创建 MappedStatement
     *
     * @param ms
     * @param sqlSource
     * @param suffix    查询id后缀 通过这个判断是统计的还是查询列表的
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource sqlSource, String suffix) {
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
        return builder.build();
    }

}
