package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@TableName("residential_building_info")
public class ResidentialBuildingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 社区
     */
    private String communityName;

    /**
     * 小区
     */
    private String estateName;

    /**
     * 层数
     */
    private Integer buildingFloor;

    /**
     * 单元数
     */
    private Integer unitNumber;

    /**
     * 地址
     */
    private String buildingAddress;

    /**
     * 户数
     */
    private Integer householdNumber;

    private String x;

    private String y;

    private String z;

//    private String latitude;
//
//    private String longitude;

    /**
     * 楼栋人数
     */
    private Integer householdRsNumber;

    private Integer focusNumber;

    @TableField(exist = false)
    private JSONObject houseData;
}
