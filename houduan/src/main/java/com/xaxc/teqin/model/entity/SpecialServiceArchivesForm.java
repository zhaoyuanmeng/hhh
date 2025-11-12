package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 档案自定义表单项信息表
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("special_service_archives_form")
public class SpecialServiceArchivesForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 档案类型父ID
     */
    private String archivesParentId;

    /**
     * 档案类型ID
     */
    private String archivesId;

    /**
     * 字段ID
     */
    private String fieldId;

    /**
     * 表单项名称
     */
    private String fieldName;

    /**
     * 是否必填(0 必填，1 非必填，2不显示)
     */
    private String mandatory;

    /**
     * 表单项类型
     */
    private String fieldType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 排序
     */
    private String sort;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 备注字段
     */
    private String remark;

    /**
     * 删除标识(0 未删，2删除)
     */
    private String delFlag;

    /**
     * '1：基本信息、2：内设机构、3：主要建筑、4：重要部位、5：四邻情况、6：停车场'
     */
    private String pid;

    /**
     * 列下标，当field为多行时使用
     */
    private Integer columnIndex;

    @TableField(exist = false)
    private List<Map<String,Object>> listFild;


}
