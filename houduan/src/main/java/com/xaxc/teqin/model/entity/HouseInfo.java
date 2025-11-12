package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

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
//@TableName("house_info")
public class HouseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 楼栋ID
     */
    private String buildingId;

    private String buildingName;

    /**
     * 楼栋单元
     */
    private String buildingCell;

    /**
     * 户主
     */
    private String ownerUserName;

    /**
     * 户主联系方式
     */
    private String ownerUserTel;

    /**
     * 身份证
     */
    private String ownerUserCardCode;

    /**
     * 房屋类型
     */
    private String roomType;

    /**
     * 房屋面积
     */
    private String roomArea;

    /**
     * 常住人数
     */
    private Integer perNumber;


    private Integer floorNumber;

    /**
     * 门牌号
     */
    private String houseNumber;

    private int focusNumber;

    @TableField(exist = false)
    private List<ResidenceInfo> residenceInfoList;

    @TableField(exist = false)
    private String title;

    private Integer checkedFlag;


}
