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
 * 档案类型信息表
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("special_service_archives")
public class SpecialServiceArchives implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 类型名称
     */
    private String archivesName;

    /**
     * 图标
     */
    private String archivesImg;

    /**
     * 排序
     */
    private int sort;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 删除标识(0 未删，2删除)
     */
    private String delFlag;

    /**
     * 点位类型（1.车站、2.桥梁、3.涵洞、4.隧道、5.基站、6.制高点、7.沿线监控设施、8.四电所 、9.疏散梯、10.复杂村镇、11.危爆物品厂库、12.高速服务区、13.无人机反制点、14.复杂路段、15.结合部协议、16.铁路平安道口、17.高铁下穿道路、18.高铁低路基路段、19.高速出入口、20.现场情况、21.住地情况）
     */
    private String kind;

    /**
     * 特殊点位的类型值，目前用于住地和现场档案基本信息里面的（停车场，主要建筑等）
     */
    private Integer specialType;


}
