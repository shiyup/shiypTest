package com.syp.test.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by shiyuping on 2024/5/8 19:58
 * OpenCV机器视觉
 * @author shiyuping
 */
public class OpencvUtil3 {

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat src = Imgcodecs.imread("/Users/mac/Downloads/Snipaste_2024-05-07_有水印.png");





        HighGui.imshow("图像亮度和对比度调节", src);
        HighGui.waitKey(0);

        // 保存图像
        //Imgcodecs.imwrite("/Users/mac/Downloads/screenshot-20240507-调整.png", result);
    }
}
