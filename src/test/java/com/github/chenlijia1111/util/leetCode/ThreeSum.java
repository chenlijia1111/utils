package com.github.chenlijia1111.util.leetCode;


import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 三数之和
 * 给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 * 例如, 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
 * <p>
 * 满足要求的三元组集合为：
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 *
 *
 * 双指针法,但是有点慢
 * 后面有一个大神写的,但是没注释,待研究
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/6 0006 下午 2:41
 **/
public class ThreeSum {

    public List<List<Integer>> threeSumV1(int[] nums) {
        Set<List<Integer>> lists = new HashSet<>();
        if (nums.length >= 3) {
            //进行排序
            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] >= 0) {
                    break;
                }
                int l = i + 1;
                int r = nums.length - 1;
                while (l < nums.length && l != r) {
                    int sum = nums[i] + nums[l] + nums[r];
                    if (sum == 0) {
                        ArrayList<Integer> integers = new ArrayList<>();
                        integers.add(nums[i]);
                        integers.add(nums[l]);
                        integers.add(nums[r]);
                        lists.add(integers);
                        l++;
                    } else if (sum > 0) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
        }
        return lists.stream().collect(Collectors.toList());
    }

    public List<List<Integer>> threeSumV2(int[] nums) {
        Set<List<Integer>> lists = new HashSet<>();
        if (nums.length >= 3) {
            //进行排序
            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] >= 0) {
                    break;
                }
                int l = i + 1;
                int r = nums.length - 1;
                while (l < nums.length && l < r) {
                    int sum = nums[i] + nums[l] + nums[r];
                    if (sum == 0) {
                        ArrayList<Integer> integers = new ArrayList<>();
                        integers.add(nums[i]);
                        integers.add(nums[l]);
                        integers.add(nums[r]);
                        integers.sort(Comparator.comparing(e -> e));
                        lists.add(integers);
                        l++;
                        r--;
                    } else if (sum > 0) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
        }
        return lists.stream().collect(Collectors.toList());
    }

    public List<List<Integer>> threeSumV3(int[] nums) {
        if (nums.length < 3) {
            return Collections.emptyList();
        }
        List<List<Integer>> res = new ArrayList<>();
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        int negSize = 0;
        int posSize = 0;
        int zeroSize = 0;
        for (int v : nums) {
            if (v < minValue) {
                minValue = v;
            }
            if (v > maxValue) {
                maxValue = v;
            }
            if (v > 0) {
                posSize++;
            } else if (v < 0) {
                negSize++;
            } else {
                zeroSize++;
            }
        }
        if (zeroSize >= 3) {
            res.add(Arrays.asList(0, 0, 0));
        }
        if (negSize == 0 || posSize == 0) {
            return res;
        }
        if (minValue * 2 + maxValue > 0) {
            maxValue = -minValue * 2;
        } else if (maxValue * 2 + minValue < 0) {
            minValue = -maxValue * 2;
        }

        int[] map = new int[maxValue - minValue + 1];
        int[] negs = new int[negSize];
        int[] poses = new int[posSize];
        negSize = 0;
        posSize = 0;
        for (int v : nums) {
            if (v >= minValue && v <= maxValue) {
                if (map[v - minValue]++ == 0) {
                    if (v > 0) {
                        poses[posSize++] = v;
                    } else if (v < 0) {
                        negs[negSize++] = v;
                    }
                }
            }
        }
        Arrays.sort(poses, 0, posSize);
        Arrays.sort(negs, 0, negSize);
        int basej = 0;
        for (int i = negSize - 1; i >= 0; i--) {
            int nv = negs[i];
            int minp = (-nv) >>> 1;
            while (basej < posSize && poses[basej] < minp) {
                basej++;
            }
            for (int j = basej; j < posSize; j++) {
                int pv = poses[j];
                int cv = 0 - nv - pv;
                if (cv >= nv && cv <= pv) {
                    if (cv == nv) {
                        if (map[nv - minValue] > 1) {
                            res.add(Arrays.asList(nv, nv, pv));
                        }
                    } else if (cv == pv) {
                        if (map[pv - minValue] > 1) {
                            res.add(Arrays.asList(nv, pv, pv));
                        }
                    } else {
                        if (map[cv - minValue] > 0) {
                            res.add(Arrays.asList(nv, cv, pv));
                        }
                    }
                } else if (cv < nv) {
                    break;
                }
            }
        }
        return res;
    }



    @Test
    public void test() {
        int[] nums = {-5, -4, -3, -3, -2, -1, 0, 1, 2, 3};
        List<List<Integer>> lists = threeSumV3(nums);
    }

}
