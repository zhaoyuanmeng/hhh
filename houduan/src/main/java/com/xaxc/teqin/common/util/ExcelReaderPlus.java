package com.xaxc.teqin.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelReaderPlus {
    private static HSSFWorkbook wb;
    private static HSSFSheet sheet;
    private static HSSFRow row;
    private static XSSFWorkbook wbx;
    private static XSSFSheet sheetx;
    private static XSSFRow rowx;

    /**
     * 获取多个sheetExcel表格数据
     * 注意：1、若单元格为空时，读出为""空字符串；列数为按最后一列计算；
     * 2、如果在最后一列前单元格合并还是数据个数还是按合并前列数算，但是合并后单元格只有左上单元格有数据；
     * 3、如果是最后一列合并单元格，则算最前面一列 ，后面列不计算 在其中，也没数据（不是""空串）
     *
     * @param filePath Excel路径
     * @return
     */
//    public ExcelData readExcel(String filePath) {
    public ExcelData readExcel(File filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ExcelData excelData = new ExcelData();
        try {
            wb = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer sheetNum = wb.getNumberOfSheets();
        excelData.setSheetSum(sheetNum);
        excelData.setFileName(filePath.getName());

        //循环获取所有sheet数据
        List<ExcelSheetData> sheetDatas = new ArrayList<>();
        for (int i = 0; i < sheetNum; i++) {
            ExcelSheetData sheetData = new ExcelSheetData();
            sheet = wb.getSheetAt(i);
            sheetData.setLineSum(sheet.getPhysicalNumberOfRows());
            sheetData.setSheetName(sheet.getSheetName());
            List<ExcelLineData> lineDatas = readExcelContentBySheet(sheet);
            sheetData.setLineData(lineDatas);
            sheetDatas.add(sheetData);
        }
        excelData.setSheetData(sheetDatas);
        return excelData;
    }

    public ExcelData csreadExcel(String filePath) {
        InputStream is = null;
        File file = new File(filePath);
        try {
            is = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ExcelData excelData = new ExcelData();
        try {
            wb = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer sheetNum = wb.getNumberOfSheets();
        excelData.setSheetSum(sheetNum);
        excelData.setFileName(file.getName());

        //循环获取所有sheet数据
        List<ExcelSheetData> sheetDatas = new ArrayList<>();
        for (int i = 0; i < sheetNum; i++) {
            ExcelSheetData sheetData = new ExcelSheetData();
            sheet = wb.getSheetAt(i);
            sheetData.setLineSum(sheet.getPhysicalNumberOfRows());
            sheetData.setSheetName(sheet.getSheetName());
            List<ExcelLineData> lineDatas = readExcelContentBySheet(sheet);
            sheetData.setLineData(lineDatas);
            sheetDatas.add(sheetData);
        }
        excelData.setSheetData(sheetDatas);
        return excelData;
    }


    private ExcelData readExcelx(XSSFWorkbook wbx, String fileName) {

        ExcelData excelData = new ExcelData();
        Integer sheetNum = wbx.getNumberOfSheets();
        excelData.setSheetSum(sheetNum);
        excelData.setFileName(fileName);

        //循环获取所有sheet数据
        List<ExcelSheetData> sheetDatas = new ArrayList<>();
        for (int i = 0; i < sheetNum; i++) {
            ExcelSheetData sheetData = new ExcelSheetData();
            sheetx = wbx.getSheetAt(i);
            sheetData.setSheetName(sheetx.getSheetName());
            sheetData.setLineSum(sheetx.getPhysicalNumberOfRows());
            List<ExcelLineData> lineDatas = readExcelContentBySheetx(sheetx);
            sheetData.setLineData(lineDatas);
            sheetDatas.add(sheetData);
        }
        excelData.setSheetData(sheetDatas);
        return excelData;
    }


    private List<ExcelLineData> readExcelContentBySheet(HSSFSheet sheet) {
        List<ExcelLineData> lineDatas = new ArrayList<>();
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i <= rowNum; i++) {
            int j = 0;
            row = sheet.getRow(i);
            if (Objects.isNull(row)) {
                continue;
            }

            int colNum = row.getLastCellNum();
            ExcelLineData lineData = new ExcelLineData();
            List<String> colData = new ArrayList<>();
            lineData.setColSum(colNum);
            while (j < colNum) {
                String value = getCellValue(row.getCell(j)).trim();
                colData.add(value);
                j++;
            }
            lineData.setColData(colData);
            lineDatas.add(lineData);
        }

        return lineDatas;
    }

    private List<ExcelLineData> readExcelContentBySheetx(XSSFSheet sheetx) {
        List<ExcelLineData> lineDatas = new ArrayList<>();
        // 得到总行数
        int rowNum = sheetx.getLastRowNum();
        for (int i = 0; i <= rowNum; i++) {
            int j = 0;
            rowx = sheetx.getRow(i);
            if (Objects.isNull(rowx)) {
                continue;
            }

            int colNum = rowx.getLastCellNum();
            ExcelLineData lineData = new ExcelLineData();
            List<String> colData = new ArrayList<>();
            lineData.setColSum(colNum);
            while (j < colNum) {
                String value = getCellValuex(rowx.getCell(j)).trim();
                colData.add(value);
                j++;
            }
            lineData.setColData(colData);
            lineDatas.add(lineData);
        }

        return lineDatas;
    }


    // 用于处理 ex 读取数字格式数据丢失经度的问题
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {

        numberFormat.setGroupingUsed(false);

    }

    /**
     * 获取单元格数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getCellValue(HSSFCell cell) {
        if (Objects.isNull(cell)) {
            return "";
        }

        String value = "";
        switch (cell.getCellType()) {
            case NUMERIC: // 数字
                //如果为时间格式的内容
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                    break;
                } else {
                    value = numberFormat.format(cell.getNumericCellValue());
                }
                break;
            case STRING: // 字符串
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
//                value = cell.getBooleanCellValue() + "";
                value = numberFormat.format(cell.getBooleanCellValue()) + "";
                break;
            case FORMULA: // 公式
                value = cell.getCellFormula() + "";
                break;
            case BLANK: // 空值
                value = "";
                break;
            case ERROR: // 故障
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }

    private String getCellValuex(XSSFCell cellx) {
        if (Objects.isNull(cellx)) {
            return "";
        }

        String value = "";
        switch (cellx.getCellType()) {
            case NUMERIC: // 数字
                //如果为时间格式的内容
                if (HSSFDateUtil.isCellDateFormatted(cellx)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value = sdf.format(HSSFDateUtil.getJavaDate(cellx.getNumericCellValue())).toString();
                    break;
                } else {
                    value = new DecimalFormat("0").format(cellx.getNumericCellValue());
                }
                break;
            case STRING: // 字符串
                value = cellx.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
                value = cellx.getBooleanCellValue() + "";
                break;
            case FORMULA: // 公式
                value = cellx.getCellFormula() + "";
                break;
            case BLANK: // 空值
                value = "";
                break;
            case ERROR: // 故障
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }



    public class ExcelData {
        private int sheetSum;
        private String fileName;
        private List<ExcelSheetData> sheetData;

        public int getSheetSum() {
            return sheetSum;
        }

        public void setSheetSum(int sheetSum) {
            this.sheetSum = sheetSum;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public List<ExcelSheetData> getSheetData() {
            return sheetData;
        }

        public void setSheetData(List<ExcelSheetData> sheetData) {
            this.sheetData = sheetData;
        }
    }


}

