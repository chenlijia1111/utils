package com.github.chenlijia1111.utils.math.treeNode;

import com.github.chenlijia1111.utils.list.Lists;

import java.util.*;

/**
 * 树节点工具类
 * 不使用递归 递归效率太低
 *
 * @author Chen LiJia
 * @see TreeNodeVo
 * @since 2020/3/16
 */
public class TreeNodeUtil {


    /**
     * 将子节点填充到父节点中去
     * 不使用递归解决问题 递归效率太低
     * 测试处理 2500 条数据只要 6 微秒，基本上是很快的了
     * <p>
     * 提示：如果不想破坏原有数据，需要深度复制一遍原有数据，再将数据传入本方法
     *
     * @param list
     * @return
     */
    public static List<TreeNodeVo> fillChildTreeNode(List<? extends TreeNodeVo> list) {

        //resultList 最终返回的顶级节点
        List<TreeNodeVo> resultList = new ArrayList<>();
        if (Lists.isNotEmpty(list)) {
            //构建map来处理问题
            Map<String, TreeNodeVo> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                TreeNodeVo treeNodeVo = list.get(i);
                map.put(treeNodeVo.getTreeId(), treeNodeVo);
            }

            //开始处理
            Iterator<? extends TreeNodeVo> iterator = list.iterator();
            while (iterator.hasNext()) {
                TreeNodeVo treeNodeVo = iterator.next();
                //父Id
                String treeParentId = treeNodeVo.getTreeParentId();
                //父节点
                TreeNodeVo parentTreeNode = map.get(treeParentId);
                if (Objects.isNull(map.get(treeParentId))) {
                    //说明父节点不存在,这个是顶级节点
                    resultList.add(treeNodeVo);
                } else {
                    //说明他不是顶级节点
                    //处理他
                    List<TreeNodeVo> childTreeNodeList = parentTreeNode.getChildTreeNodeList();
                    childTreeNodeList.add(treeNodeVo);
                    parentTreeNode.setChildTreeNodeList(childTreeNodeList);
                }
            }
        }
        return resultList;
    }


    /**
     * 查询指定父节点的下级列表
     * 测试处理 2500 条数据只要 6 微秒，基本上是很快的了
     * <p>
     * 提示：如果不想破坏原有数据，需要深度复制一遍原有数据，再将数据传入本方法
     *
     * @param parentTreeNodeId 父节点id  指定查找这个父节点的下级列表
     * @param list
     * @return
     */
    public static List<TreeNodeVo> findChildTreeNode(String parentTreeNodeId, List<? extends TreeNodeVo> list) {

        //resultList 最终返回的顶级节点
        List<TreeNodeVo> resultList = new ArrayList<>();
        if (Lists.isNotEmpty(list)) {
            //构建map来处理问题
            Map<String, TreeNodeVo> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                TreeNodeVo treeNodeVo = list.get(i);
                map.put(treeNodeVo.getTreeId(), treeNodeVo);
            }

            //开始处理
            Iterator<? extends TreeNodeVo> iterator = list.iterator();
            while (iterator.hasNext()) {
                TreeNodeVo treeNodeVo = iterator.next();
                //父Id
                String treeParentId = treeNodeVo.getTreeParentId();
                //父节点
                TreeNodeVo parentTreeNode = map.get(treeParentId);
                if (Objects.isNull(map.get(treeParentId))) {
                    //说明父节点不存在,这个是顶级节点
                    resultList.add(treeNodeVo);
                } else {
                    //说明他不是顶级节点
                    //处理他
                    List<TreeNodeVo> childTreeNodeList = parentTreeNode.getChildTreeNodeList();
                    childTreeNodeList.add(treeNodeVo);
                    parentTreeNode.setChildTreeNodeList(childTreeNodeList);
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            TreeNodeVo treeNodeVo = list.get(i);
            if (Objects.equals(treeNodeVo.getTreeId(), parentTreeNodeId)) {
                //返回他的下级列表
                return treeNodeVo.getChildTreeNodeList();
            }
        }
        return new ArrayList<>();
    }

}
