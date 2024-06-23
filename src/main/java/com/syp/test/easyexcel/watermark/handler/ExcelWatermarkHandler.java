package com.syp.test.easyexcel.watermark.handler;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;
import com.syp.test.easyexcel.watermark.constant.WatermarkParam;
import com.syp.test.easyexcel.watermark.content.CommonWatermarkContent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author wangyw
 * @Since 2021/11/5
 *
 * excel添加水印策略类.
 * 可在多种场景下使用，提供示例如下：
 * （1）easyexcel使用场景：
 *          EasyExcel.write(excelPathName,clazz)
 *                   .inMemory(true)
 *                   .registerWriteHandler(new ExcelWatermarkHandler("watermark content"))
 *                   .sheet("Sheet1")
 *                   .doWrite(dataList);
 * （2）POI excel使用场景（可为workbook的各个Sheet添加水印；只支持XSSFWorkbook）：
 *          ExcelWatermarkHandler.putWatermarkToWorkbook(workbook,watermarkContent);
 */

@Slf4j
//@RequiredArgsConstructor
public class ExcelWatermarkHandler implements SheetWriteHandler, RowWriteHandler, CellWriteHandler {

    /**
     * 水印内容
     */
    private final String watermarkContent;

    /**
     * 写入数据多少
     */
    private final int dataSize;

    /**
     * 随机行数
     */
    private final int randomRow;

    public ExcelWatermarkHandler(String watermarkContent, int dataSize) {
        this.watermarkContent = watermarkContent;
        this.dataSize = dataSize;
        //从数据行的第二行开始
        this.randomRow = RandomUtil.randomInt(2, dataSize);
    }



    /**
     * 根据用户姓名，工号，创建日期信息生成水印图片
     *
     * @param contentObj    用户姓名，工号，创建日期信息
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream createWaterMark(CommonWatermarkContent contentObj) throws IOException {
        return createWaterMark(contentObj.toString());
    }

    /**
     *
     * @param content                   水印内容
     * @return byteArrayOutputStream    水印图片流，使用后请关闭
     * @throws IOException
     */
    public static ByteArrayOutputStream createWaterMark(String content) throws IOException {

        BufferedImage waterMarkImage = new BufferedImage(WatermarkParam.width, WatermarkParam.height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2d = waterMarkImage.createGraphics();
        waterMarkImage = graphics2d.getDeviceConfiguration().createCompatibleImage(WatermarkParam.width, WatermarkParam.height, Transparency.TRANSLUCENT);
        graphics2d.dispose();
        graphics2d = waterMarkImage.createGraphics();
        graphics2d.setColor(WatermarkParam.color);
        graphics2d.setStroke(WatermarkParam.stroke);
        graphics2d.setFont(WatermarkParam.font);
        graphics2d.rotate(WatermarkParam.theta, (double) waterMarkImage.getWidth() / 2, (double) waterMarkImage.getHeight() / 2);
        FontRenderContext context = graphics2d.getFontRenderContext();
        Rectangle2D bounds = WatermarkParam.font.getStringBounds(content, context);
        double x = (WatermarkParam.width - bounds.getWidth()) / 2;
        double y = (WatermarkParam.height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        graphics2d.drawString(content, (int) x, (int) baseY);

        graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        graphics2d.dispose();
        // 读取图像
        //BufferedImage waterMarkImage = ImageIO.read(new File("/Users/mac/Downloads/头像暗水印50.png"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(waterMarkImage, WatermarkParam.format, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * 水印策略，为XSSFSheet添加水印
     *
     * @param sheet     待添加水印的sheet
     * @param bytes     水印图片字节
     */
    public static void putWatermarkToExcel(XSSFSheet sheet, byte[] bytes) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        PackageRelationship pr = sheet.getPackagePart().
                addRelationship(workbook.getAllPictures().get(pictureIdx).getPackagePart().getPartName(),
                        TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);

        sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
    }

    /**
     * 为XSSFWorkbook的每个Sheet添加水印
     *
     * @param workbook          待添加水印的workbook
     * @param watermarkContent  水印内容
     */
    public static void putWatermarkToWorkbook(XSSFWorkbook workbook,String watermarkContent) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = createWaterMark(watermarkContent)) {
            int pictureIdx = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
            for(int i = 0;i < workbook.getNumberOfSheets();i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                PackageRelationship pr = sheet.getPackagePart().addRelationship(workbook.getAllPictures().get(pictureIdx).getPackagePart().getPartName(), TargetMode.INTERNAL, XSSFRelation.IMAGES.getRelation(), null);
                sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
            }
        }
    }

    @SneakyThrows
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        ByteArrayOutputStream watermark = null;
        try {
            watermark = createWaterMark(watermarkContent);
            XSSFSheet sheet = (XSSFSheet) writeSheetHolder.getSheet();
            //显示水印
            putWatermarkToExcel(sheet, watermark.toByteArray());

            //addTextBox(sheet);
            } catch(Exception e) {
            log.error("[ExcelWatermarkHandler] afterSheetCreate()",e);
        } finally {
            if(watermark != null) {
                watermark.close();
            }
        }
    }

    private void addTextBox(XSSFSheet sheet) {
        //插入文本框
        //列开始值
        int colStart = 5;
        //创建画布
        XSSFDrawing draw = sheet.createDrawingPatriarch();
        //创建锚点（0，0，0，0，col1, row1, col2, row2）
        /*创建一个新的客户端锚，并设置左上角和右下角
         *通过单元格引用和偏移量锚定的坐标。
         * @param dx1第一个单元格内的x坐标。
         * @param dy1第一个单元格内的y坐标。
         * @param dx2第二个单元格内的x坐标。
         * @param dy2第二个单元格内的y坐标。
         * @param col1第一个单元格的列（从0开始）。
         * @param row1第一个单元格的行（从0开始）。
         * @param col2第二个单元格的列（从0开始）。
         * @param row2第二个单元格的行（从0开始）。
         */
        XSSFClientAnchor createAnchor = draw.createAnchor(0, 0, 0, 0, colStart, 11, colStart + 6, 11+7);
        //创建文本框
        XSSFTextBox tb1 = draw.createTextbox(createAnchor);
        //设置透明度
        // 设置透明背景色

        //设置边框颜色，黑色
        tb1.setLineStyleColor(0, 0, 0);
        //设置边框宽度
        //tb1.setLineWidth(2);

        //设置填充色，白色
        Color col = Color.white;
        tb1.setFillColor(col.getRed(), col.getGreen(), col.getBlue());
        //富文本字符串
        XSSFRichTextString address = new XSSFRichTextString("测试");
        tb1.setText(address);
        //文字字符属性
        CTTextCharacterProperties rpr = tb1.getCTShape().getTxBody().getPArray(0).getRArray(0).getRPr();
        //设置字体
        rpr.addNewLatin().setTypeface("Trebuchet MS");
        //设置字体透明度
        //设置字体大小9pt
        rpr.setSz(900);
        //设置字体颜色，蓝色
        col = Color.BLACK;
        rpr.addNewSolidFill().addNewSrgbClr().setVal(new byte[]{(byte)col.getRed(),(byte)col.getGreen(),(byte)col.getBlue()});
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //是否是表头
        if (!isHead){
            return;
        }
        //嵌入隐水印
        String cellValue = cell.getStringCellValue();
        cell.setCellValue(TextBlindWatermarkUtil.embed(cellValue, "lm6pqw"));
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                         Integer relativeRowIndex, Boolean isHead) {
        if (isHead){
            return;
        }
        //relativeRowIndex从0开始算
        //某个数据行后往后两列写入(除第一行数据列)
        if (relativeRowIndex + 1 == randomRow){
            Cell newCell = row.createCell(row.getLastCellNum() + 1);
            addWatermarkToExcel(newCell, writeSheetHolder.getSheet());
        }
        //最后一行数据往后两行某列写入
        if (relativeRowIndex + 1 == dataSize){
            //最后一行数据填写完成后再下面生成一条新行，插入水印
            Row newRow = writeSheetHolder.getSheet().createRow(row.getRowNum() + 2);
            //从第2列到最后一列之间选择单元格插入水印
            Cell newCell = newRow.createCell(RandomUtil.randomInt(1, row.getLastCellNum()));
            addWatermarkToExcel(newCell, writeSheetHolder.getSheet());
        }


    }

    private void addWatermarkToExcel(Cell newCell, Sheet sheet) {
        newCell.setCellValue(TextBlindWatermarkUtil.getWm( "lm6pqw"));
        newCell.setCellType(CellType.STRING);
        Workbook workbook = sheet.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);
        newCell.setCellStyle(cellStyle);
    }
}