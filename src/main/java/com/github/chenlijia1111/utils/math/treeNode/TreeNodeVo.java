package com.github.chenlijia1111.utils.math.treeNode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 树形对象
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class TreeNodeVo {

    /**
     * 当前id
     */
    private String treeId;

    /**
     * 父亲id
     * 顶级节点父节点为空 或者父节点不存在
     */
    private String treeParentId;

    /**
     * 子节点集合
     * 返回前端的时候隐藏它，好看点
     */
    @JsonIgnore
    private List<TreeNodeVo> childTreeNodeList;

    public TreeNodeVo() {
    }

    public TreeNodeVo(String treeId, String treeParentId) {
        this.treeId = treeId;
        this.treeParentId = treeParentId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getTreeParentId() {
        if (Objects.equals(this.treeParentId, "null")) {
            // 防止序列化是变为 null
            this.treeParentId = null;
        }
        return treeParentId;
    }

    public void setTreeParentId(String treeParentId) {
        this.treeParentId = treeParentId;
    }

    public List<TreeNodeVo> getChildTreeNodeList() {
        if (null == childTreeNodeList) {
            childTreeNodeList = new ArrayList<>();
        }
        return childTreeNodeList;
    }

    public void setChildTreeNodeList(List<TreeNodeVo> childTreeNodeList) {
        this.childTreeNodeList = childTreeNodeList;
    }

    @Override
    public String toString() {
        return "TreeNodeVo{" +
                "treeId='" + treeId + '\'' +
                ", treeParentId='" + treeParentId + '\'' +
                ", childTreeNodeList=" + childTreeNodeList +
                '}';
    }
}
