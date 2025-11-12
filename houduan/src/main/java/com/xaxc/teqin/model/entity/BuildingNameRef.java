package com.xaxc.teqin.model.entity;

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
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("building_name_ref")
public class BuildingNameRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图层标签名
     */
    @TableId(value = "layer_tag_name", type = IdType.INPUT)
    private String layerTagName;

    /**
     * 数据源楼栋名
     */
    private String buildingName;


}
