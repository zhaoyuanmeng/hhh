package com.xaxc.teqin.model.entity;

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
@TableName("commercial_building_info")
public class CommercialBuildingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(exist = false)
    private String tenant;

    @TableField(exist = false)
    private Integer status;

    /**
     * 区域ID
     */
    private String regionId;

    private String buildingName;

    private String x;

    private String y;

    private String z;

    /**
     * 楼层数
     */
    private Integer floorNumber;

    /**
     * 抽出标志
     */
    private Integer pullOutFlag;


}
