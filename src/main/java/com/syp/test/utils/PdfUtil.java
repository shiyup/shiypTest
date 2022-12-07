package com.syp.test.utils;

import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;


/**
 * itext  转PDF 工具类
 */
public class PdfUtil {

    static String word_path = "D:\\shiyuping\\testdoc.doc";

    static String excel_path = "D:\\shiyuping\\testxlsx.xlsx";

    static String ppt_path = "D:\\shiyuping\\testppt.ppt";

    static String pptx_path = "D:\\shiyuping\\testpptx.pptx";

    static String pdf_path = "D:\\shiyuping\\pdf\\pptx.pdf";

    public static void main(String[] args) {
         pptToPdf(ppt_path);
        //docToPdf(word_path);
        //excelToPdf(excel_path);
    }

    public static File Pdf(ArrayList<String> imageUrllist, String mOutputPdfFileName) {
        //Document doc = new Document(PageSize.A4, 20, 20, 20, 20); // new一个pdf文档
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            // pdf写入
            PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
            // 打开文档
            doc.open();
            // 循环图片List，将图片加入到pdf中
            for (int i = 0; i < imageUrllist.size(); i++) {
                // 在pdf创建一页
                doc.newPage();
                // 通过文件路径获取image
                Image png1 = Image.getInstance(imageUrllist.get(i));
                float heigth = png1.getHeight();
                float width = png1.getWidth();
                int percent = getPercent2(heigth, width);
                png1.setAlignment(Image.MIDDLE);
                // 表示是原来图像的比例;
                png1.scalePercent(percent + 3);
                doc.add(png1);
            }
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 输出流
        File mOutputPdfFile = new File(mOutputPdfFileName);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        // 反回文件输出流
        return mOutputPdfFile;
    }

    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }


    /**
     * 图片文件转PDF
     *
     * @param filepath
     * @param request
     * @return
     */
    public static String imgToPdf(String filepath, HttpServletRequest request) {
        boolean result = false;
        String pdfUrl = "";
        String fileUrl = "";
        try {
            // 图片list集合
            ArrayList<String> imageUrllist = new ArrayList<String>();
            // 添加图片文件路径
            imageUrllist.add(request.getSession().getServletContext().getRealPath(File.separator + filepath));
            String fles = filepath.substring(0, filepath.lastIndexOf("."));
            // 输出pdf文件路径
            pdfUrl = request.getSession().getServletContext().getRealPath(File.separator + fles + ".pdf");
            fileUrl = fles + ".pdf";
            result = true;
            if (result == true) {
                // 生成pdf
                File file = PdfUtil.Pdf(imageUrllist, pdfUrl);
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
	
	
 
	
	/*public static void doc2pdf(String Address, String outPath) {
		if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
			return;
		}
		try {
			long old = System.currentTimeMillis();
			File file = new File(outPath); // 新建一个空白pdf文档
			FileOutputStream os = new FileOutputStream(file);
			Document doc = new Document(Address); // Address是将要被转化的word文档
			doc.save(
					os,
					SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML,
			// OpenDocument, PDF, EPUB, XPS, SWF
			// 相互转换
			long now = System.currentTimeMillis();
			System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

    public static boolean getLicense() {
        boolean result = false;

        try {
            InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String docToPdf(String filePath) {

        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return "PDF格式转化失败";
        }
        try {
            long old = System.currentTimeMillis();
            String filePath2 = filePath.substring(0, filePath.lastIndexOf("."));
            String pdfPathString = filePath2 + ".pdf";
            // 输出pdf文件路径
            filePath2 = filePath2 + ".pdf";
            // 新建一个空白pdf文档
            File file = new File(filePath2);
            FileOutputStream os = new FileOutputStream(file);
            // Address是将要被转化的word文档
            Document doc = new Document(filePath);
            // 全面支持DOC, DOCX, OOXML, RTF HTML,
            doc.save(os, SaveFormat.PDF);
            // OpenDocument, PDF, EPUB, XPS, SWF
            // 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
            os.close();

            return pdfPathString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PDF格式转化失败";
    }


    public static boolean getLicense1() {
        boolean result = false;
        try {
            InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream("classpath:/license.xml");
            com.aspose.cells.License aposeLic = new com.aspose.cells.License();
            aposeLic.setLicense(is);
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 转换有问题，横向太长了会被pdf切割成好几页
     */
    public static String excelToPdf(String filePath) {
        if (!getLicense1()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
            return "PDF格式转化失败";
        }
        try {
            long old = System.currentTimeMillis();
            //获取路径参数
            String filePath2 = filePath.substring(0, filePath.lastIndexOf("."));
            String pdfSPath = filePath2 + ".pdf";
            // 输出pdf文件路径
            filePath2 = filePath2 + ".pdf";

            //文件操作
            // 新建一个空白pdf文档
            File file = new File(filePath2);
            // 原始excel路径
            Workbook wb = new Workbook(filePath);
            FileOutputStream fileOS = new FileOutputStream(file);
            wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
            fileOS.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
            return pdfSPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PDF格式转化失败";
    }

    public static boolean getLicense2() {
        boolean result = false;
        try {
            InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            com.aspose.slides.License aposeLic = new com.aspose.slides.License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     */
    public static String pptToPdf(String filePath) {
        // 验证License
        if (!getLicense2()) {
            return "PDF格式转化失败";
        }
        try {
            long old = System.currentTimeMillis();
            String filePath2 = filePath.substring(0, filePath.lastIndexOf("."));
            String pdfPathString = filePath2 + ".pdf";
            //文件操作
            // 新建一个空白pdf文档
            File file = new File(pdfPathString);
            //输入pdf路径
            com.aspose.slides.Presentation pres = new com.aspose.slides.Presentation(filePath);

            FileOutputStream fileOS = new FileOutputStream(file);
            pres.save(fileOS, com.aspose.slides.SaveFormat.Pdf);
            fileOS.close();

            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + file.getPath()); //转化过程耗时
            return pdfPathString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PDF格式转化失败";
    }
}