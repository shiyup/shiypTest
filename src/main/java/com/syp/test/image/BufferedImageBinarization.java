package com.syp.test.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像二值化处理
 * Created by shiyuping on 2024/4/28 11:09 AM
 *
 * @author shiyuping
 */
public class BufferedImageBinarization {

    public static void main(String[] args) throws IOException {
        // 读取图像
        BufferedImage image = ImageIO.read(new File("/Users/mac/Downloads/Snipaste_2024-04-30_09-37-03.png"));

        BufferedImage imageNew = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        // 遍历每一个像素
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // 获取当前像素的RGB值
                int rgb = image.getRGB(x, y);

                // 将RGB值转换为灰度值
                int gray = (rgb >> 16) & 0xFF;
                //int gray = (Color.blue(rgb) + Color.green(rgb) + Color.red(rgb)) / 3;

                // 二值化处理：这里以128为阈值
                int binaryColor = (gray < 128) ? 0 : 255;
                int binaryRgb = (binaryColor << 16) | (binaryColor << 8) | binaryColor;
                imageNew.setRGB(x, y, binaryRgb);
            }
        }

        // 保存二值图像
        ImageIO.write(imageNew, "jpg", new File("/Users/mac/Downloads/暗水图片二值化10.jpg"));
    }
}
