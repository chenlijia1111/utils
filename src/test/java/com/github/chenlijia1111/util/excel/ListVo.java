package com.github.chenlijia1111.util.excel;

import com.github.chenlijia1111.utils.office.excel.annos.ExcelExportField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 日志列表对象
 *
 * @author Chen LiJia
 * @since 2020/7/16
 */
@ApiModel
public class ListVo {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Integer id;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @ExcelExportField(titleHeadName = "操作人", sort = 1)
    private String operateUserName;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @ExcelExportField(titleHeadName = "操作时间", sort = 2)
    private Date createTime;

    /**
     * 操作动作
     */
    @ApiModelProperty(value = "操作动作")
    @ExcelExportField(titleHeadName = "操作动作", sort = 3)
    private String operateName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }
}
