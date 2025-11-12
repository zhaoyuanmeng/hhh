package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xaxc.teqin.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("residential_building_position")
public class ResidentialBuildingPosition extends BaseEntity {

    private String buildingName;

    @TableId(value = "building_address", type = IdType.INPUT)
    private String buildingAddress;
}
