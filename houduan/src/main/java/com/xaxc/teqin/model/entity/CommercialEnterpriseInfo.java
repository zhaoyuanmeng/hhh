package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("commercial_enterprise_info")
public class CommercialEnterpriseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String industry;

    private String productType;

    private String head;

    private String phone;

    private Integer settledFlag;

    private Integer keyFlag;

    private String area;

    private Integer type;

    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate signingTime;

    @TableField(exist = false)
    private List<String> positions;


    @TableField(exist = false)
    private String title;

    private Integer checkedFlag;


    @TableField(exist = false)
    private String x;

    @TableField(exist = false)
    private String y;

    @TableField(exist = false)
    private String z;


}
