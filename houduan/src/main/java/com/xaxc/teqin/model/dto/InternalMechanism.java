package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 内设机构
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalMechanism implements Serializable {
    /**
     * 部门
     */
    private String mingcheng;

    /**
     * 人数
     */
    private String renshu;

    /**
     * 负责人
     */
    private String fuzeren;

    /**
     * 联系电话
     */
    private String lianxidianhua;
}
