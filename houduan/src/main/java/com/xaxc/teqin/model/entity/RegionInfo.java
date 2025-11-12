package com.xaxc.teqin.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
@TableName("region_info")
public class RegionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 区域名称
     */
    @ExcelProperty(index = 0)
    private String name;

    /**
     * 社区、商服
     */
    private String type;

    @ExcelProperty(index = 1)
    private String x;

    @ExcelProperty(index = 2)
    private String y;

    @ExcelProperty(index = 3)
    private String z;


}
