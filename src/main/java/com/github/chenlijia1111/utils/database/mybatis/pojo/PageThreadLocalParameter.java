package com.github.chenlijia1111.utils.database.mybatis.pojo;

/**
 * 分页线程变量
 * @author Chen LiJia
 * @since 2019/12/27
 */
public class PageThreadLocalParameter {

    /**
     * 线程变量
     * 保存分页参数
     */
    private static final ThreadLocal<Page> PAGE_PARAMETER = new ThreadLocal<>();

    /**
     * 取出线程变量
     * @return
     */
    public static Page getPageParameter() {
        return PAGE_PARAMETER.get();
    }

    /**
     * 删除线程变量
     */
    public static void removePageParameter() {
        PAGE_PARAMETER.remove();
    }

    /**
     * 赋值线程变量
     * @param pageParameter
     */
    public static void setPageParameter(Page pageParameter) {
        PAGE_PARAMETER.set(pageParameter);
    }

}
