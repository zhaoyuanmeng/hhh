package com.xaxc.teqin.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xaxc.teqin.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends User {

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("roleCode")
    private String roleCode;

    @JsonProperty("resources")
    private String resources;

    @JsonProperty("orgShortName")
    private String orgShortName;

    @JsonProperty("orgFullName")
    private String orgFullName;

    @JsonProperty("token")
    private String token;

    @JsonProperty("keyword")
    private String keyword;


}
