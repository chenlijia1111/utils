package com.github.chenlijia1111.utils.database.mybatis;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.database.mybatis.pojo.Page;
import com.github.chenlijia1111.utils.database.mybatis.pojo.PageThreadLocalParameter;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 执行器工具类
 *
 * @author Chen LiJia
 * @since 2020/5/28
 */
public class ExecutorUtil {

    private static Field additionalParametersField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("获取 BoundSql 属性 additionalParameters 失败: " + e, e);
        }
    }

    /**
     * 获取 BoundSql 属性值 additionalParameters
     *
     * @param boundSql
     * @return
     */
    public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) {
        try {
            return (Map<String, Object>) additionalParametersField.get(boundSql);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("获取 BoundSql 属性值 additionalParameters 失败: " + e, e);
        }
    }

    /**
     * 尝试获取已经存在的在 MS，提供对手写count和page的支持
     *
     * @param configuration
     * @param msId
     * @return
     */
    public static MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
        MappedStatement mappedStatement = null;
        try {
            mappedStatement = configuration.getMappedStatement(msId, false);
        } catch (Throwable t) {
            //ignore
        }
        return mappedStatement;
    }

    /**
     * 执行手动设置的 count 查询，该查询支持的参数必须和被分页的方法相同
     *
     * @param executor
     * @param countMs
     * @param parameter
     * @param boundSql
     * @param resultHandler
     * @return
     * @throws SQLException
     */
    public static Long executeManualCount(Executor executor, MappedStatement countMs,
                                          Object parameter, BoundSql boundSql,
                                          ResultHandler resultHandler) throws SQLException {
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = countMs.getBoundSql(parameter);
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = ((Number) ((List) countResultList).get(0)).longValue();
        return count;
    }

    /**
     * 执行自动生成的 count 查询
     *
     * @param executor
     * @param countMs
     * @param parameter
     * @param boundSql
     * @param rowBounds
     * @param resultHandler
     * @return
     * @throws SQLException
     */
    public static Long executeAutoCount(Executor executor, MappedStatement countMs,
                                        Object parameter, BoundSql boundSql,
                                        RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
        //创建 count 查询的缓存 key
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        //调用方言获取 count sql
        String countSql = getCountSql(boundSql);
        //countKey.update(countSql);
        BoundSql countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        //当使用动态 SQL 时，可能会产生临时的参数，这些参数需要手动设置到新的 BoundSql 中
        for (String key : additionalParameters.keySet()) {
            countBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
        //执行 count 查询
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = (Long) ((List) countResultList).get(0);
        return count;
    }

    /**
     * 获取计算总数sql
     *
     * @param boundSql
     * @return
     */
    private static String getCountSql(BoundSql boundSql) {
        String sql = boundSql.getSql();

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("SELECT COUNT(*) FROM (");
        sqlStringBuilder.append(sql);
        sqlStringBuilder.append(") temp");
        return sqlStringBuilder.toString();
    }

    /**
     * 获取查询sql
     *
     * @param boundSql
     * @return
     */
    private static String getPageSql(BoundSql boundSql) {
        //取线程变量
        Page pageParameter = PageThreadLocalParameter.getPageParameter();
        AssertUtil.isTrue(Objects.nonNull(pageParameter), "未启用分页");

        String sql = boundSql.getSql();

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append(sql);
        sqlStringBuilder.append(" LIMIT ");
        sqlStringBuilder.append(pageParameter.getOffset());
        sqlStringBuilder.append(",");
        sqlStringBuilder.append(pageParameter.getLimit());
        return sqlStringBuilder.toString();
    }


    /**
     * 分页查询
     *
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     * @param cacheKey
     * @param <E>
     * @return
     * @throws SQLException
     */
    public static <E> List<E> pageQuery(Executor executor, MappedStatement ms, Object parameter,
                                        RowBounds rowBounds, ResultHandler resultHandler,
                                        BoundSql boundSql, CacheKey cacheKey) throws SQLException {
        //判断是否需要进行分页查询
        Page page = PageThreadLocalParameter.getPageParameter();
        if (Objects.nonNull(page)) {
            //生成分页的缓存 key
            CacheKey pageKey = cacheKey;
            //处理参数对象
            parameter = processParameterObject(ms, parameter, boundSql, pageKey);
            //调用获取分页 sql
            String pageSql = getPageSql(boundSql);
            BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);

            Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
            //设置动态参数
            for (String key : additionalParameters.keySet()) {
                pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
            }
            //执行分页查询
            return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
        } else {
            //不执行分页的情况下，也不执行内存分页
            return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, boundSql);
        }
    }


    public static Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        //处理参数
        Page page = PageThreadLocalParameter.getPageParameter();
        //没有分页，不需要处理
        if (Objects.isNull(page)) {
            return parameterObject;
        }
        Map<String, Object> paramMap = null;
        if (parameterObject == null) {
            paramMap = new HashMap<>();
        } else if (parameterObject instanceof Map) {
            //解决不可变Map的情况
            paramMap = new HashMap<>();
            paramMap.putAll((Map) parameterObject);
        } else {
            paramMap = new HashMap<>();
            //动态sql时的判断条件不会出现在ParameterMapping中，但是必须有，所以这里需要收集所有的getter属性
            //TypeHandlerRegistry可以直接处理的会作为一个直接使用的对象进行处理
            boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
            MetaObject metaObject = MetaObjectUtil.forObject(parameterObject);
            //需要针对注解形式的MyProviderSqlSource保存原值
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            //下面这段方法，主要解决一个常见类型的参数时的问题
            if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                    String name = parameterMapping.getProperty();
                        if (hasTypeHandler
                                || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                            paramMap.put(name, parameterObject);
                            break;
                        }
                }
            }
        }
        return paramMap;
    }

}
