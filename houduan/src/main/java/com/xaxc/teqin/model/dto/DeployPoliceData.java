package com.xaxc.teqin.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

@Data
public class DeployPoliceData implements Serializable {

    @NonNull
    private String taskId;
    @NonNull
    private String sceneId;
    @NonNull
    private String basicDataId;

    private List<JSONObject> policeData;

}
