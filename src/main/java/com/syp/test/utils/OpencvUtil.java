package com.syp.test.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by shiyuping on 2024/5/8 18:05
 *
 * @author shiyuping
 */
public class OpencvUtil {

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        //light("/Users/mac/Downloads/网页截图隐水印.png", "/Users/mac/Downloads/网页截图-水印还原2.png");
        light("/Users/mac/Downloads/excel截图6.png", "/Users/mac/Downloads/Excel截图6-还原.png");
    }

    /**
     * OpenCV-4.5.5 图像亮度和对比度调节
     */
    public static void light(String srcPath, String dstPath){
        // --------------------------------基于伽马变换提取水印-----------------------------------------------
        Mat image = Imgcodecs.imread(srcPath);

        Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
        byte[] lookUpTableData = new byte[(int) (lookUpTable.total()*lookUpTable.channels())];
        for (int i = 0; i < lookUpTable.cols(); i++) {
            //调整伽马值130.0
            lookUpTableData[i] = saturate(Math.pow(i / 255.0, 160.0) * 255.0);
        }

        lookUpTable.put(0, 0, lookUpTableData);
        Mat dst = new Mat();
        Core.LUT(image, lookUpTable, dst);
        // --------------------------------基于颜色的图像分割-----------------------------------------------
        // 转换颜色空间从BGR到HSV
        Mat hsv = new Mat();
        Imgproc.cvtColor(dst, hsv, Imgproc.COLOR_RGB2HSV);

        // 定义灰色范围
        Scalar lowerGray = new Scalar(0, 0, 135);
        Scalar upperGray = new Scalar(180, 43, 220);

        // 根据颜色范围创建掩码
        Mat result = new Mat();
        Core.inRange(hsv, lowerGray, upperGray, result);

        // 保存图像
        Imgcodecs.imwrite(dstPath, result);
        hsv.release();
        result.release();
        image.release();
        dst.release();
    }

    // 将 double 数值转换为 byte
    private static byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }
}
