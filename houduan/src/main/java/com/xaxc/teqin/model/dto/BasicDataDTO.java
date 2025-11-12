package com.xaxc.teqin.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicDataDTO {

    private String id;

    private String type;

    private String name;

    private List coordinates;

    private List<BasicDataDTO> children;

    private int num;

    private String buildName;

    private String floorNum;

    private String groupId;

    private JSONObject middlePoint;

    private JSONObject data;

    private String dataLevelFlag;


//    private String startDistance;
//
//    private String endDistance;


}
