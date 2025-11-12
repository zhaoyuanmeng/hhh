package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-11-26
 */
@Data
public class Region implements Serializable {

    /**
     * 区域名称
     */
    @ExcelProperty(index = 0)
    private String name;


    @ExcelProperty(index = 1)
    private String x;

    @ExcelProperty(index = 2)
    private String y;

    @ExcelProperty(index = 3)
    private String z;


}
