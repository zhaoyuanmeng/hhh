package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 四邻情况信息
 *
 * @author jyx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeighborhoodInfo implements Serializable {
    /**
     * 东临
     */
    private Neighborhood donglin;

    /**
     * 南邻
     */
    private Neighborhood nanlin;

    /**
     * 西邻
     */
    private Neighborhood xilin;

    /**
     * 北邻
     */
    private Neighborhood beilin;
}
