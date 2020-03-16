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
            while (list.size() > 0) {
                TreeNodeVo treeNodeVo = list.get(list.size() - 1);
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
                }
                //在列表中删除这个节点，表明已经处理过了，给他找到了父级节点
                list.remove(list.size() - 1);
            }
        }
        return resultList;
    }

}
