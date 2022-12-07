package com.syp.test.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author shiyuping
 * @Date 2022/12/6 14:04
 */
@Slf4j
public class PdfConvertUtil {

    static String word_path="D:\\shiyuping\\test.docx";

    static String ppt_path="D:\\shiyuping\\test.pptx";

    static String pdf_path="D:\\shiyuping\\pdf\\pptx1.pdf";

    public static void main(String[] args) {
        //docx转换有问题
        /*try{
            ByteArrayInputStream bais = new ByteArrayInputStream(docxToPdf(new FileInputStream(new File(word_path))));
            //新建文件
            File file = new File(pdf_path);
            if (file.exists()){
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024 * 1024];
            //先读后写
            while ((read = bais.read(bytes)) > 0){
                byte[] wBytes = new byte[read];
                System.arraycopy(bytes, 0, wBytes, 0, read);
                os.write(wBytes);
            }
            os.flush();
            os.close();
            bais.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //会有抬头
        convertPPTToPDF(new File(ppt_path), new File(pdf_path));
    }



    public static boolean convertPPTToPDF(File file, File toFile) {
        try {
            Document pdfDocument = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(toFile));
            FileInputStream is = new FileInputStream(file);
            HSLFSlideShow hslfSlideShow = convertPPTToPDFByPPT(is);
            double zoom = 2;
            if (hslfSlideShow == null) {
                is = new FileInputStream(file);
                XMLSlideShow ppt = convertPPTToPDFByPPTX(is);
                if (ppt == null) {
                    throw new NullPointerException("This PPTX get data is error....");
                }
                Dimension pgsize = ppt.getPageSize();
                List<XSLFSlide> slide = ppt.getSlides();
                AffineTransform at = new AffineTransform();
                at.setToScale(zoom, zoom);
                pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
                pdfWriter.open();
                pdfDocument.open();
                PdfPTable table = new PdfPTable(1);
                for (XSLFSlide xslfSlide : slide) {
                    BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setTransform(at);

                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    xslfSlide.draw(graphics);
                    graphics.getPaint();
                    Image slideImage = Image.getInstance(img, null);
                    table.addCell(new PdfPCell(slideImage, true));
                }
                ppt.close();
                pdfDocument.add(table);
                pdfDocument.close();
                pdfWriter.close();
                System.out.println(file.getAbsolutePath() + "Powerpoint file converted to PDF successfully");
                return true;
            }

            Dimension pgsize = hslfSlideShow.getPageSize();
            List<HSLFSlide> slides = hslfSlideShow.getSlides();
            pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
            pdfWriter.open();
            pdfDocument.open();
            AffineTransform at = new AffineTransform();
            PdfPTable table = new PdfPTable(1);
            for (HSLFSlide hslfSlide : slides) {
                BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setTransform(at);

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                hslfSlide.draw(graphics);
                graphics.getPaint();
                Image slideImage = Image.getInstance(img, null);
                table.addCell(new PdfPCell(slideImage, true));
            }
            hslfSlideShow.close();
            pdfDocument.add(table);
            pdfDocument.close();
            pdfWriter.close();
            System.out.println(file.getAbsolutePath() + "Powerpoint file converted to PDF successfully");
            return true;
        } catch (Exception e) {
            log.error("ppt转为pdf异常：{}",e);
            return false;
        }
    }

    private static XMLSlideShow convertPPTToPDFByPPTX(FileInputStream is) {
        try {
            return new XMLSlideShow(is);
        } catch (IOException e) {
            return null;
        }
    }

    private static HSLFSlideShow convertPPTToPDFByPPT(FileInputStream is) {
        try {
            return new HSLFSlideShow(is);
        } catch (Exception e) {
            return null;
        }
    }
}
