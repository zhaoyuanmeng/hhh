package com.xaxc.teqin.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTimeConverter implements Converter<LocalDateTime> {
    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (null == cellData) {
            return null;
        }
        LocalDateTime result = null;
        if (cellData.getType() == CellDataTypeEnum.NUMBER) {
            if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
                Date date = DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(),
                        globalConfiguration.getUse1904windowing(), null);
                result = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else {
                Date date = DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(),
                        contentProperty.getDateTimeFormatProperty().getUse1904windowing(), null);
                result = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        }
        if (cellData.getType() == CellDataTypeEnum.STRING) {
            String value = cellData.getStringValue();
            if (value.contains("-")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                result = LocalDateTime.parse(value, formatter);
            } else if (value.contains("/")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                result = LocalDateTime.parse(value, formatter);
            }
        }
        return result;
    }
}
