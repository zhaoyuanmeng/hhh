package com.xaxc.teqin.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JsonObjectConverter implements Converter<JSONObject> {
    @Override
    public Class<JSONObject> supportJavaTypeKey() {
        return JSONObject.class;
    }

    @Override
    public JSONObject convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (null == cellData) {
            return null;
        }
        return JSONObject.parseObject(cellData.getStringValue());
    }
}
