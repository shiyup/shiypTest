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
public class OpencvUtil2 {

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat image = Imgcodecs.imread("/Users/mac/Downloads/Snipaste_2024-05-07_有水印.png");
        // 转换颜色空间从BGR到HSV
        Mat hsv = new Mat();
        Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);

        // 定义灰色颜色的HSV值
        Scalar lowerGray = new Scalar(0, 0, 140);
        Scalar upperGray = new Scalar(180, 43, 220);

        // 根据颜色范围创建掩码
        Mat maskGray = new Mat();
        Core.inRange(hsv, lowerGray, upperGray, maskGray);

        // 应用掩码
        Mat result = new Mat();
        Core.bitwise_and(image, image, result, maskGray);

        HighGui.imshow("图像亮度和对比度调节", result);
        HighGui.waitKey(0);

        // 保存图像
        //Imgcodecs.imwrite("/Users/mac/Downloads/screenshot-20240507-调整.png", result);
    }
}
