package com.sun.sort.bubbleSort;

/**
 * @Title 冒泡排序
 * 基本思想:
 * 对当前还未排好序的范围内的全部数，自上而下对相邻的俩个数依次进行比较和调整，让较大的数下沉，
 * 较小的数往上冒。即：每当俩相邻的数比较后发现他们的排序与排序的要求相反时，就将他们交换。
 * 每次遍历都可确定一个最大值放到待排数组的末尾，下次遍历，对该最大值以及它之后的元素不再排序（已经排好）。
 * ---------------------
 * @author SunTao
 * @date 2018/10/25 16:29
 */
public class Sort {

    private int[] array;

    public Sort(int[] array) {
        this.array = array;
    }

    //按顺序打印数组中的元素
    public void display() {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println();
    }

    /*
待排数组中一共有7个数，第一轮排序时进行了6次比较，第二轮排序时进行了5比较，依次类推，最后一轮进行了一次比较。
加入元素总数为N，则一共需要的比较次数为：   (N-1)+ (N-2)+ (N-3)+ ...1=N*(N-1)/2
这样，算法约做了N2/2次比较。因为只有在前面的元素比后面的元素大时才交换数据，所以交换的次数少于比较的次数。
如果数据是随机的，大概有一半数据需要交换，则交换的次数为N2/4（不过在最坏情况下，即初始数据逆序时，每次比较都需要交换）。
交换和比较的操作次数都与N2成正比，由于在大O表示法中，常数忽略不计，冒泡排序的时间复杂度为O(N2)。
O(N2)的时间复杂度是一个比较糟糕的结果，尤其在数据量很大的情况下。所以冒泡排序通常不会用于实际应用。
     */
    public void bubbleSort() {
        int temp;
        int len = array.length;

        for (int i = 0; i < len - 1; i++) {  //外层循环：每循环一次就确定了一个相对最大元素
            for (int j = 1; j < len - i; j++) {  //内层循环：有i个元素已经排好，根据i确定本次的比较次数
                if (array[j - 1] > array[j]) {  //如果前一位大于后一位，交换位置
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
            System.out.print("第" + (i + 1) + "轮排序结果：");
            display();
        }
    }

    /*
    冒泡排序的改进1
上面已经分析过，冒泡排序的效率比较低，所以我们要通过各种方法改进。
最简单的改进方法是加入一标志性变量exchange，用于标志某一趟排序过程中是否有数据交换，
如果进行某一趟排序时并没有进行数据交换，则说明数据已经按要求排列好，可立即结束排序，避免不必要的比较过程
在上例中，第四轮排序之后实际上整个数组已经是有序的了，最后两轮的比较没必要进行。
     */
    public void bubbleSort_improvement_1() {
        int temp;
        int len = array.length;

        for (int i = 0; i < len - 1; i++) {
            boolean exchange = false;  //设置交换变量
            for (int j = 1; j < len - i; j++) {
                if (array[j - 1] > array[j]) {  //如果前一位大于后一位，交换位置
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;

                    if (!exchange) {
                        exchange = true;  //发生了交换操作
                    }
                }
                System.out.print("第" + (i + 1) + "轮排序结果：");
                display();
                if (!exchange) {
                    break;  //如果上一轮没有发生交换数据，证明已经是有序的了，结束排序
                }
            }
        }
    }

    /*
    冒泡排序改进2
第1轮排序之后，11、18、20已经是有序的了，后面的几次排序后它们的位置都没有变化，但是根据冒泡算法，
18依然会在第2轮参与比较，11依然会在第2轮、第3轮参与比较，其实都是无用功。
我们可以对算法进一步改进：设置一个pos指针，pos后面的数据在上一轮排序中没有发生交换，下一轮排序时，
就对pos之后的数据不再比较。
     */
    //冒泡排序改进2
    public void bubbleSort_improvement_2() {
        int temp;
        int counter = 1;
        int endPoint = array.length - 1;  //endPoint代表最后一个需要比较的元素下标

        while (endPoint > 0) {
            int pos = 1;
            for (int j = 1; j <= endPoint; j++) {
                if (array[j - 1] > array[j]) {  //如果前一位大于后一位，交换位置
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;

                    pos = j;  //下标为j的元素与下标为j-1的元素发生了数据交换
                }
            }
            endPoint = pos - 1;  //下一轮排序时只对下标小于pos的元素排序，下标大于等于pos的元素已经排好

            System.out.print("第" + counter + "轮排序结果：");
            display();
        }
    }

    /*
        冒泡排序改进3
    对的算法来说，没有最好，只有更好。上面的两种改进方法其实治标不治本，是一种“扬汤止沸”的改进。
    传统的冒泡算法每次排序只确定了最大值，我们可以在每次循环之中进行正反两次冒泡，
    分别找到最大值和最小值，如此可使排序的轮数减少一半。
     */
    //冒泡排序改进3
    public void bubbleSort_improvement_3() {
        int temp;
        int low = 0;
        int high = array.length - 1;
        int counter = 1;
        while (low < high) {

            for (int i = low; i < high; ++i) {   //正向冒泡，确定最大值
                if (array[i] > array[i + 1]) {  //如果前一位大于后一位，交换位置
                    temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
            --high;

            for (int j = high; j > low; --j) {   //反向冒泡，确定最小值
                if (array[j] < array[j - 1]) {  //如果前一位大于后一位，交换位置
                    temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
            ++low;

            System.out.print("第" + counter + "轮排序结果：");
            display();
            counter++;
        }
    }

}