package com.syp.test.utils;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.GaussianBlur;

/**
 * org.bytedeco.javacpp
 * Created by shiyuping on 2024/5/8 19:58
 * OpenCV机器视觉
 * @author shiyuping
 */
public class OpencvUtil3 {

    public static void main(String[] args) {
        Mat img = opencv_imgcodecs.imread("/Users/mac/Downloads/excel截图7.png");
        if (img.empty()) {
            System.out.println("Image not loaded");
        }
        // 先提取出水印
        double gamma = 130.0;

        // 具体做法先归一化到1,然后gamma作为指数值求出新的像素值再还原
        Mat gammaTable = new Mat(1, 256, opencv_core.CV_8UC1);
        for (int i = 0; i < 256; i++) {
            double value = Math.pow(i / 255.0, gamma) * 255.0;
            gammaTable.ptr(0, i).put((byte)Math.round(value));
        }
        // 实现映射用的是OpenCV的查表函数
        Mat newImg = new Mat();
        opencv_core.LUT(img, gammaTable, newImg);

        // 转换颜色空间从BGR到HSV
        Mat hsv = new Mat();
        opencv_imgproc.cvtColor(newImg, hsv, opencv_imgproc.COLOR_BGR2HSV);

        // 定义灰色范围
        Scalar lowerGray = new Scalar(0, 0, 135, 0);
        Scalar upperGray = new Scalar(180, 43, 220, 0);

        // 根据颜色范围创建掩码
        Mat result = new Mat();
        opencv_core.inRange(hsv, new Mat(lowerGray), new Mat(upperGray), result);

        // 应用高斯模糊
        Mat blurredImg = new Mat();
        GaussianBlur(result, blurredImg, new Size(3, 3), 0);

        // 应用拉普拉斯算子进行锐化
        Mat laplacian = new Mat();
        opencv_imgproc.Laplacian(blurredImg, laplacian, opencv_core.CV_16S, 3, 1.0, 0, opencv_core.BORDER_DEFAULT);
        Mat sharpImg = new Mat();
        // 转换为绝对值
        opencv_core.convertScaleAbs(laplacian, sharpImg);

        // 保存处理后的图像
        String outputFilePath = "/Users/mac/Downloads/excel截图7-锐化.png";
        imwrite(outputFilePath, sharpImg);

    }
}
