package com.syp.test.image.watermark;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
/**
 * Created by shiyuping on 2024/4/29 5:38 PM
 *
 * @author shiyuping
 */

/**
 * 添加明水印util
 */
public class WatermarkUtil {

    public static void main(String[] args) {

        // 本地图片路径：
        String localPath = "/Users/mac/Downloads/无背景透明图片的明水印.png";
        // 网络图片地址：
        String networkPath = "https://img0.baidu.com/it/u=3708545959,316194769&fm=253&fmt=auto&app=138&f=PNG?w=1000&h=1000";
        // 文字水印内容
        String textWatermark = "这里是水印";
        // 图片水印路径
        String pictureWatermark = "C:\\Users\\admin\\Desktop\\wm.jpg";

        // 本地图片 添加文字水印
        addWatermark(1, 1, localPath, textWatermark);

        // 网络图片 添加文字水印
        //addWatermark(2, 1, networkPath, textWatermark);

        // 本地图片 添加图片水印
        //addWatermark(1, 2, localPath, pictureWatermark);

        // 网络图片 添加图片水印
        //addWatermark(2, 2, networkPath, pictureWatermark);

    }

    /**
     * 读取本地图片
     *
     * @param path 本地图片存放路径
     */
    public static Image readLocalPicture(String path) {
        if (null == path) {
            throw new RuntimeException("本地图片路径不能为空");
        }
        // 读取原图片信息 得到文件
        File srcImgFile = new File(path);
        try {
            // 将文件对象转化为图片对象
            return ImageIO.read(srcImgFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取网络图片
     *
     * @param path 网络图片地址
     */
    public static Image readNetworkPicture(String path) {
        if (null == path) {
            throw new RuntimeException("网络图片路径不能为空");
        }
        try {
            // 创建一个URL对象,获取网络图片的地址信息
            URL url = new URL(path);
            // 将URL对象输入流转化为图片对象 (url.openStream()方法，获得一个输入流)
            BufferedImage bugImg = ImageIO.read(url.openStream());
            if (null == bugImg) {
                throw new RuntimeException("网络图片地址不正确");
            }
            return bugImg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 水印处理
     *
     * @param image     图片对象
     * @param type      水印类型（1-文字水印 2-图片水印）
     * @param watermark 水印内容（文字水印内容/图片水印的存放路径）
     */
    public static String manageWatermark(Image image, Integer type, String watermark) {
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 加水印：
        // 创建画笔
        Graphics2D graphics = bufImg.createGraphics();
        // 绘制原始图片
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);

        // 校验水印的类型
        if (type == 1) {
            if (StringUtils.isEmpty(watermark)) {
                throw new RuntimeException("文字水印内容不能为空");
            }
            // 添加文字水印：
            // 根据图片的背景设置水印颜色
            graphics.setColor(new Color(255, 255, 255, 128));
            // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt
            graphics.setFont(new Font("微软雅黑", Font.BOLD, 45));
            // 设置水印的坐标(为原图片中间位置)
            int x = (imgWidth - getWatermarkLength(watermark, graphics)) / 2;
            int y = imgHeight / 2;
            // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标
            graphics.drawString(watermark, x, y);
            graphics.dispose();
        } else {
            // 添加图片水印：
            if (StringUtils.isEmpty(watermark)) {
                throw new RuntimeException("图片水印存放路径不能为空");
            }
            Image srcWatermark = readLocalPicture(watermark);
            int watermarkWidth = srcWatermark.getWidth(null);
            int watermarkHeight = srcWatermark.getHeight(null);
            // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
            // 绘制水印图片  坐标为中间位置
            graphics.drawImage(srcWatermark, (imgWidth - watermarkWidth) / 2,
                    (imgHeight - watermarkHeight) / 2, watermarkWidth, watermarkHeight, null);
            graphics.dispose();
        }
        // 定义存储的地址
        String tarImgPath = "/Users/mac/Downloads/watermark.png";
        // 输出图片
        try {
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, "png", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return "水印添加成功";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加水印
     *
     * @param pictureType   图片来源类型（1-本地图片 2-网络图片）
     * @param watermarkType 水印类型（1-文字水印 2-图片水印）
     * @param path          图片路径
     * @param watermark     水印内容（文字水印内容/图片水印的存放路径）
     */
    public static String addWatermark(Integer pictureType, Integer watermarkType, String path, String watermark) {
        if (null == pictureType) {
            throw new RuntimeException("图片来源类型不能为空");
        }
        if (null == watermarkType) {
            throw new RuntimeException("水印类型不能为空");
        }

        Image image;
        if (pictureType == 1) {
            // 读取本地图片
            image = readLocalPicture(path);
        } else {
            // 读取网络图片
            image = readNetworkPicture(path);
        }
        if (watermarkType == 1) {
            // 添加文字水印
            return manageWatermark(image, 1, watermark);
        } else {
            // 添加图片水印
            return manageWatermark(image, 2, watermark);
        }
    }

    /**
     * 获取水印文字的长度
     *
     * @param watermarkContent 文字水印内容
     * @param graphics         图像类
     */
    private static int getWatermarkLength(String watermarkContent, Graphics2D graphics) {
        return graphics.getFontMetrics(graphics.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());
    }


}