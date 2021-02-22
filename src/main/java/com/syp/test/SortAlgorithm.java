package com.syp.test;

import java.util.Arrays;

/**
 * created by shiyuping on 2019/1/22
 */
public class SortAlgorithm {

    public static void main(String[] args){
        int[] a = {1,3,2,7,9,4,6,8,5,11,12,13,14,15,16};
        selectionSort(a);
        System.out.println(Arrays.toString(a));
    }


    /**
     * 冒泡排序
     * <p>
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * <p>
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。
     * <p>
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * <p>
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较
     *
     * @param a
     */
    public static void bubbleSort(int[] a) {

        for (int i = 0; i < a.length; i++) {
            boolean flag = true;
            for (int j = 0; j < a.length - i - 1; j++) {
                int tmp;
                if (a[j] > a[j+1]) {
                    tmp = a[j+1];
                    a[j+1] = a[j];
                    a[j] = tmp;
                    flag = false;
                }
            }
            //如果没有数据交换则跳过
            if (flag){
                break;
            }
        }
    }

    /**
     * 插入排序
     * 将第一待排序序列第一个元素看做一个有序序列，把第二个元素到最后一个元素当成是未排序序列。
     * <p>
     * 从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置。
     * （如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面。）
     *
     * @param a
     */
    public static void insertionSort(int[] a) {

        for (int i = 1; i < a.length; i++) {
            int value = a[i];
            int j = i-1;
            //遍历插入位置
            for (; j >= 0; j--) {
                if (a[j]>value){
                    a[j+1]=a[j];//移动
                }else {
                    break;
                }
            }
            a[j+1]= value;
        }
    }

    /**
     * 选择排序
     * 1.首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置
     * <p>
     * 2.再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
     * <p>
     * 3.重复第二步，直到所有元素均排序完毕。
     *
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        // 总共要经过 N-1 轮比较
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            // 每轮需要比较的次数 N-i
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    // 记录目前能找到的最小值元素的下标
                    min = j;
                }
            }
            // 将找到的最小值和i位置所在的值进行交换
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
    }

    /**
     * 希尔排序
     * 缩小增量排序
     */
    public static void shellSort(int[] arr) {
        int gap = 1;
        while (gap < arr.length / 3) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) {
                int tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = tmp;
            }
            gap = (int) Math.floor(gap / 3);
        }
    }
}
