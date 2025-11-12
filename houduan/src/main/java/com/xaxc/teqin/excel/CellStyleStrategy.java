package com.xaxc.teqin.excel;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

/**
 * - 设置表头和填充内容的样式
 */
public class CellStyleStrategy extends HorizontalCellStyleStrategy {

    private final WriteCellStyle headWriteCellStyle;
    private final WriteCellStyle contentWriteCellStyle;

    /**
     * 操作列
     */
    private final List<Integer> rowIndexes;

    public CellStyleStrategy(List<Integer> rowIndexes, WriteCellStyle headWriteCellStyle, WriteCellStyle contentWriteCellStyle) {
        this.rowIndexes = rowIndexes;
        this.headWriteCellStyle = headWriteCellStyle;
        this.contentWriteCellStyle = contentWriteCellStyle;
    }

    //设置头样式
    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        // 获取字体实例
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        if (rowIndexes.get(0).equals(context.getRowIndex())) {
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headWriteFont.setFontHeightInPoints((short) 18);
            headWriteFont.setBold(true);
            headWriteCellStyle.setBorderTop(BorderStyle.NONE);
            headWriteCellStyle.setBorderRight(BorderStyle.NONE);
            headWriteCellStyle.setBorderBottom(BorderStyle.NONE);
        } else if (rowIndexes.get(1).equals(context.getRowIndex())) {
            headWriteFont.setFontHeightInPoints((short) 6);
            headWriteFont.setBold(true);
            headWriteCellStyle.setBorderTop(BorderStyle.NONE);
            headWriteCellStyle.setBorderRight(BorderStyle.NONE);
            headWriteCellStyle.setBorderBottom(BorderStyle.NONE);
        } else {
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headWriteFont.setFontHeightInPoints((short) 11);
            headWriteFont.setBold(true);
            headWriteCellStyle.setBorderTop(BorderStyle.THIN);
            headWriteCellStyle.setBorderRight(BorderStyle.THIN);
            headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            //设置文字颜色
//            if(context.getColumnIndex() == 1){
//                headWriteFont.setColor(IndexedColors.RED.getIndex());
//            }
        }
        headWriteCellStyle.setWriteFont(headWriteFont);
        if (stopProcessing(context)) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(headWriteCellStyle, cellData.getOrCreateStyle());
    }

    //设置填充数据样式
    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 11);
        //设置数据填充后的实线边框
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(contentWriteCellStyle, cellData.getOrCreateStyle());
    }
}
