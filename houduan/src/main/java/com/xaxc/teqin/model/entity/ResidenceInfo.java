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
 * @since 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@TableName("residence_info")
public class ResidenceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 性别
     */
    private String sex;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 户籍地
     */
    private String registerAddress;

    /**
     * 职务
     */
    private String post;

    /**
     * 工作单位
     */
    private String workUnit;

    /**
     * 房屋ID
     */
    private String houseId;

    /**
     * 户主标志
     */
    private String ownerFlag;

    private String focusFlag;

    /**
     * 学历
     */
    private String educationName;
    /**
     * 民族
     */
    private String nationName;
    /**
     * 就业状态
     */
    private String workStatusName;
    /**
     * 就业类型
     */
    private String workTypeName;
    /**
     * 身份证
     */
    private String identityCard;
    /**
     * 行业类别
     */
    private String industryTypeName;

}
