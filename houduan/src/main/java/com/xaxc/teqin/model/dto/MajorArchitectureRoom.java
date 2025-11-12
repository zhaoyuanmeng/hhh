package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主要建筑房间
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorArchitectureRoom implements Serializable {
    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 座位
     */
    private String zuowei;
}
