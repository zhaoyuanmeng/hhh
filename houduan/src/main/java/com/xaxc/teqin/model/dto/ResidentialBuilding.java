package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class ResidentialBuilding implements Serializable {

    /**
     * 楼栋名称
     */
    @ExcelProperty(index = 0)
    private String buildingName;

    /**
     * 社区
     */
    @ExcelProperty(index = 1)
    private String communityName;

    /**
     * 小区
     */
    @ExcelProperty(index = 2)
    private String estateName;

    /**
     * 层数
     */
    @ExcelProperty(index = 3)
    private Integer buildingFloor;

    /**
     * 单元数
     */
    @ExcelProperty(index = 4)
    private Integer unitNumber;

    /**
     * 地址
     */
    @ExcelProperty(index = 5)
    private String buildingAddress;

    /**
     * 户数
     */
    @ExcelProperty(index = 6)
    private Integer householdNumber;


    /**
     * 楼栋人数
     */
    @ExcelProperty(index = 7)
    private Integer householdRsNumber;

    @ExcelProperty(index = 8)
    private String x;

    @ExcelProperty(index = 9)
    private String y;

    @ExcelProperty(index = 10)
    private String z;

    @ExcelIgnore
    private String id;


}
