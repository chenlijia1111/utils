package com.github.chenlijia1111.utils.database.mybatis.pojo;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.util.Objects;

/**
 * 页面查询信息
 * 将其存储在线程变量中 {@link PageThreadLocalParameter#getPageParameter()}
 *
 * @author Chen LiJia
 * @since 2019/12/25
 */
public class Page {


    /**
     * 起始位置
     */
    private Integer offset;

    /**
     * 当前页
     * 从 1 开始
     */
    private Integer page;

    /**
     * 每页的数量
     */
    private Integer limit;

    /**
     * 通过计算得出的总页数
     */
    private Integer count;

    private Page() {
    }

    /**
     * 创建分页对象
     * 并保存到线程变量中
     * #{@link PageThreadLocalParameter#setPageParameter(Page)}
     *
     * @param offset 起始位置
     * @param page   当前页
     * @param limit  每页的数量
     * @return
     */
    public static Page startPage(Integer offset, Integer page, Integer limit) {

        AssertUtil.isTrue(Objects.nonNull(offset), "起始位置不能为空");
        AssertUtil.isTrue(Objects.nonNull(page), "当前页不能为空");
        AssertUtil.isTrue(Objects.nonNull(limit) && !Objects.equals(limit, 0), "每页的数量不合法");

        Page pageDomain = new Page();
        pageDomain.offset = offset;
        pageDomain.page = page;
        pageDomain.limit = limit;

        //保存对象到线程变量中  等待消费
        PageThreadLocalParameter.setPageParameter(pageDomain);
        return pageDomain;
    }

    /**
     * 创建分页对象
     *
     * @param page  当前页
     * @param limit 每页的数量
     * @return
     */
    public static Page startPage(Integer page, Integer limit) {
        AssertUtil.isTrue(Objects.nonNull(page), "当前页不能为空");
        AssertUtil.isTrue(Objects.nonNull(limit) && !Objects.equals(limit, 0), "每页的数量不合法");

        //计算起始位置
        Integer offset = (page - 1) * limit;
        return startPage(offset, page, limit);
    }

    public Integer getOffset() {
        return offset;
    }


    public Integer getPage() {
        return page;
    }


    public Integer getLimit() {
        return limit;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
