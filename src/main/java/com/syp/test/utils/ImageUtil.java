package com.syp.test.utils;



import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by shiyuping on 2024/5/7 17:31
 *
 * @author shiyuping
 */
public class ImageUtil {

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        light();
    }

//    public static void main(String[] args) throws IOException{
//        String inputPath = "/Users/mac/Downloads/screenshot-20240507-144920.png";
//        String outputPath = "/Users/mac/Downloads/screenshot-调整.png";
//
//        // Calling method 2 inside main() to
//        // adjust the brightness of image
//        AdjustBrightness(inputPath, outputPath);
//    }

    /**
     * OpenCV-4.0.0 图像亮度和对比度调节
     */
    public static void light(){
        Mat src = Imgcodecs.imread("/Users/mac/Downloads/screenshot-20240507-144920.png");
        Mat dst = new Mat(src.size(), src.type());
        int channels = src.channels();//获取图像通道数
        double[] pixel = new double[3];
        float alpha=1.2f;
        float bate=10f;
        for (int i = 0, rlen = src.rows(); i < rlen; i++) {
            for (int j = 0, clen = src.cols(); j < clen; j++) {
                if (channels == 3) {//1 图片为3通道即平常的(R,G,B)
                    pixel = src.get(i, j).clone();
                    pixel[0] = pixel[0]*alpha+bate;//R
                    pixel[1] = pixel[1]*alpha+bate;//G
                    pixel[2] = pixel[2]*alpha+bate;//B
                    dst.put(i, j, pixel);
                } else {//2 图片为单通道即灰度图
                    pixel=src.get(i, j).clone();
                    dst.put(i, j, pixel[0]*alpha+bate);
                }
            }
        }
        HighGui.imshow("图像亮度和对比度调节", dst);
        HighGui.waitKey(1);
    }
}
