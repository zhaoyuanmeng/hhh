package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主要建筑
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorArchitecture implements Serializable {
    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 建筑面积
     */
    private String jianzhumianji;

    /**
     * 层数
     */
    private String cengshu;

    /**
     * 客房
     */
    private String kefang;

    /**
     * 容纳人数
     */
    private String rongnarenshu;

    /**
     * 贵宾休息室
     */
    private MajorArchitectureRoom guibinxiuxishi;

    /**
     * 会议室
     */
    private MajorArchitectureRoom huiyishi;

    /**
     * 餐厅
     */
    private MajorArchitectureRoom canting;
}
