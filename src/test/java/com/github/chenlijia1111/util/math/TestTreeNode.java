package com.github.chenlijia1111.util.math;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.math.treeNode.TreeNodeUtil;
import com.github.chenlijia1111.utils.math.treeNode.TreeNodeVo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试父子节点
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class TestTreeNode {


    @Test
    public void test1(){

        long l = System.currentTimeMillis();

        List<TreeNodeVo> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new TreeNodeVo(String.valueOf(i),null));

            //子集
            for (int j = 0; j < 50; j++) {
                list.add(new TreeNodeVo(String.valueOf(10 * i + j),String.valueOf(i)));

                //子集
                for (int k = 0; k < 5; k++) {
                    list.add(new TreeNodeVo(String.valueOf(10 * i + 5 * j + k),String.valueOf(10 * i + j)));
                }
            }
        }

        List<TreeNodeVo> treeNodeVos = TreeNodeUtil.fillChildTreeNode(list);
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(JSONUtil.objToStr(treeNodeVos));
    }

}
