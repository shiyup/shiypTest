package com.syp.test;

import java.util.Arrays;

/**
 * created by shiyuping on 2019/1/22
 */
public class sort {

    public static void main(String[] args){
        int[] a = {1,3,2,7,9,4,6,8,5,11,12,13,14,15,16};
        insertionSort(a);
        System.out.println(Arrays.toString(a));
    }


    //冒泡
    public static void bubbleSort(int[] a){
        
        for (int i=0;i<a.length;i++){
            boolean flag = true;
            for (int j=0;j<a.length-i-1;j++){
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

    //插入
    public static void insertionSort(int[] a){

        for (int i=1;i<a.length;i++){
            int value = a[i];
            int j = i-1;
            //遍历插入位置
            for(;j>=0;j--){
                if (a[j]>value){
                    a[j+1]=a[j];//移动
                }else {
                    break;
                }
            }
            a[j+1]= value;
        }


    }
}
