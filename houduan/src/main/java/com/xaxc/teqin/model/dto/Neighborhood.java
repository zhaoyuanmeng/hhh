package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 四邻情况数据
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Neighborhood implements Serializable {
    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 制高点
     */
    private String zhigaodian;

    /**
     * 重点人
     */
    private String zhongdianren;

    /**
     * 标识 -- 用于区分 东、南、西、北
     */
    private String fangwei;
}
