package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("commercial_room_info")
public class CommercialRoomInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String roomCode;

    private String roomTitle;

    private String floorCode;

    private String floorTitle;

    private String buildingCode;

    private String buildingTitle;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String x;

    private String y;

    private String z;

    private String areaStructure;

    private String areaUsable;

    /**
     * 0办公1商业
     */
    private Integer merchantType;

    private String linkId;

    private String height;

    private String load;

    private String water;

    private String wasteOil;

    private String wasteIndustrial;

    private String wasteSewage;

    private String strongElectricity;

    private String sizeLampblack;

    private String fuelGas;

    private String buildingId;
    @TableField(exist = false)
    private String buildingName;

    private Integer floorNumber;

    @TableField(exist = false)
    private String tenant;
    @TableField(exist = false)
    private Integer status;


}
