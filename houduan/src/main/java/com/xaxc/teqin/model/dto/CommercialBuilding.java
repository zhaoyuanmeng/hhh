package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
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
public class CommercialBuilding implements Serializable {

    /**
     * 商业区
     */
    @ExcelProperty(index = 0)
    private String regionName;

    @ExcelIgnore
    private String regionId;

    /**
     * 楼栋名称
     */
    @ExcelProperty(index = 1)
    private String buildingName;
    /**
     * 层数
     */
    @ExcelProperty(index = 2)
    private Integer floorNumber;


    @ExcelProperty(index = 3)
    private String x;

    @ExcelProperty(index = 4)
    private String y;

    @ExcelProperty(index = 5)
    private String z;

    @ExcelProperty(index = 6)
    private String pullOut;

    @ExcelIgnore
    private Integer pullOutFlag;


}
