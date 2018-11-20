package com.sun.sort.bubbleSort;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/25 16:31
 */
public class TestBubbleSort {
    public static void main(String[] args) {
        int [] a = {1,5,4,11,2,20,18};
        Sort sort = new Sort(a);
        System.out.print("未排序时的结果：");
        sort.display();
        sort.bubbleSort();

    }

}
