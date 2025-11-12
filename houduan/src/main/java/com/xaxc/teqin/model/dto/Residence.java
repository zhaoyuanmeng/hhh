package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
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
public class Residence implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String houseId;

    /**
     * 楼栋名称
     */
    @ExcelProperty(index = 0)
    private String buildingName;

    /**
     * 楼栋单元
     */
    @ExcelProperty(index = 1)
    private String buildingCell;

    /**
     * 门牌号
     */
    @ExcelProperty(index = 2)
    private Integer houseNumber;

    /**
     * 姓名
     */
    @ExcelProperty(index = 3)
    private String name;

    /**
     * 联系电话
     */
    @ExcelProperty(index = 4)
    private String tel;

    /**
     * 性别
     */
    @ExcelProperty(index = 5)
    private String sex;

    /**
     * 籍贯
     */
    @ExcelProperty(index = 6)
    private String nativePlace;

    /**
     * 国籍
     */
    @ExcelProperty(index = 7)
    private String nationality;

    /**
     * 户籍地
     */
    @ExcelProperty(index = 8)
    private String registerAddress;

    /**
     * 工作单位
     */
    @ExcelProperty(index = 9)
    private String workUnit;

    /**
     * 职务
     */
    @ExcelProperty(index = 10)
    private String post;

    @ExcelProperty(index = 11)
    private String ownerFlag;

    @ExcelProperty(index = 12)
    private String focusFlag;


}
