package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-11-26
 */
@Data
public class CommercialRoomLease implements Serializable {

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

    @ExcelIgnore
    private String buildingId;
    /**
     * 层数
     */
    @ExcelProperty(index = 2)
    private Integer floorNumber;


    @ExcelProperty(index = 3)
    private String roomTitle;


    @ExcelProperty(index = 4)
    private String x;

    @ExcelProperty(index = 5)
    private String y;

    @ExcelProperty(index = 6)
    private String z;

    @ExcelProperty(index = 7)
    private String areaStructure;

    @ExcelProperty(index = 8)
    private String tenant;
    @ExcelProperty(index = 9)
    private String industry;
    @ExcelProperty(index = 10)
    private String productType;
    @ExcelProperty(index = 11)
    private String head;
    @ExcelProperty(index = 12)
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ExcelProperty(index = 13)
    private LocalDate signingTime;
    @ExcelProperty(index = 14)
    private String signingArea;

    @ExcelProperty(index = 15)
    private String key;
    @ExcelProperty(index = 16)
    private String settled;
    @ExcelProperty(index = 17)
    private String checked;
    @ExcelProperty(index = 18)
    private String type;

    @ExcelIgnore
    private Integer keyFlag;
    /**
     * 是否入驻
     */
    @ExcelIgnore
    private Integer settledFlag;
    @ExcelIgnore
    private Integer checkedFlag;

}
