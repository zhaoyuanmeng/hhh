package com.xaxc.teqin.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaxc.teqin.model.entity.ResidenceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ExcelIgnore
    private String buildingId;


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


    @ExcelProperty(index = 2)
    private Integer floorNumber;

    /**
     * 门牌号
     */
    @ExcelProperty(index = 3)
    private Integer houseNumber;

    /**
     * 户主
     */
    @ExcelProperty(index = 4)
    private String ownerUserName;

    /**
     * 户主联系方式
     */
    @ExcelProperty(index = 5)
    private String ownerUserTel;

    /**
     * 房屋类型
     */
    @ExcelProperty(index = 6)
    private String roomType;

    /**
     * 房屋面积
     */
    @ExcelProperty(index = 7)
    private String roomArea;

    /**
     * 常住人数
     */
    @ExcelProperty(index = 8)
    private Integer perNumber;


    @ExcelProperty(index = 9)
    private String checkedFlag;


}
