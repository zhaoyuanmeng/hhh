package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.excel.JsonObjectConverter;
import com.xaxc.teqin.excel.LocalDateTimeConverter;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

@Data
public class BasicPoint implements Serializable {

    @ExcelProperty("_id")
    private String _id;
    @ExcelProperty("childTypeId")
    private String childTypeId;
    @ExcelProperty(value = "createTime", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
    @ExcelProperty(value = "data", converter = JsonObjectConverter.class)
    private JSONObject data;
    @ExcelProperty("jcxxId")
    private String jcxxId;
    @ExcelProperty("parentTypeId")
    private String parentTypeId;


}
