package com.syp.test.image;

import com.syp.test.easyexcel.watermark.constant.WatermarkParam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by shiyuping on 2024/4/28 8:59 PM
 *
 * @author shiyuping
 */
public class BufferedImageTest {


    public static void main(String[] args) throws IOException {
        // 创建水印图片
        BufferedImage waterMarkImage = new BufferedImage(WatermarkParam.width, WatermarkParam.height, BufferedImage.TYPE_INT_ARGB);
        // 创建画布
        Graphics2D graphics2d = waterMarkImage.createGraphics();
        // 设置背景为透明
        graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        // 释放对象
        graphics2d.dispose();
        ImageIO.write(waterMarkImage, "png", new File("/Users/mac/Downloads/无背景透明图片.png"));
    }
}
