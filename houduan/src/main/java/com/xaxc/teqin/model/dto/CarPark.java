package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 停车场
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPark implements Serializable {
    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 车位
     */
    private String chewei;
}
