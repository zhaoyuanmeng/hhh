package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 重要部位
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportantPart implements Serializable {
    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 所处位置
     */
    private String suochuweizhi;

    /**
     * 安全设施
     */
    private String anquansheshi;

    /**
     * 负责人
     */
    private String fuzeren;

    /**
     * 联系电话
     */
    private String lianxidianhua;
}
