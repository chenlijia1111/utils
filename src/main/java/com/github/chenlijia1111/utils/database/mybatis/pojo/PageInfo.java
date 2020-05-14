package com.github.chenlijia1111.utils.database.mybatis.pojo;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.util.List;
import java.util.Objects;

/**
 * 分页对象
 *
 * @author Chen LiJia
 * @since 2019/12/25
 */
public class PageInfo<E> {


    /**
     * 当前页码 从1开始
     */
    private Integer page;

    /**
     * 每页的数量
     */
    private Integer limit;

    /**
     * 总条数
     */
    private Integer count;

    /**
     * 总页数
     */
    private Integer allPage;

    /**
     * 数据
     */
    private List<E> list;


    private PageInfo() {
    }

    /**
     * @param list
     */
    public PageInfo(List<E> list) {

        //获取线程变量,进行消费
        Page pageParameter = PageThreadLocalParameter.getPageParameter();
        AssertUtil.isTrue(Objects.nonNull(pageParameter), "未启用分页");

        AssertUtil.isTrue(Objects.nonNull(pageParameter.getCount()), "没有进行分页查询,获取总条数失败");

        this.list = list;
        this.page = pageParameter.getPage();
        this.limit = pageParameter.getLimit();
        this.count = pageParameter.getCount();

        //计算总页数
        this.allPage = (count + limit - 1) / limit;

        //分页完之后,消费了线程变量,需要将其删除
        PageThreadLocalParameter.removePageParameter();
    }

    /**
     * 自定义构建分页数据，不使用sql分页
     * @param page
     * @param limit
     * @param count
     * @param list
     */
    public PageInfo(Integer page, Integer limit, Integer count, List<E> list) {
        this.page = page;
        this.limit = limit;
        this.count = count;
        this.list = list;
        //计算总页数
        this.allPage = (count + limit - 1) / limit;
    }

    public Integer getCount() {
        return count;
    }


    public Integer getAllPage() {
        return allPage;
    }


    public List<E> getList() {
        return list;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }
}
