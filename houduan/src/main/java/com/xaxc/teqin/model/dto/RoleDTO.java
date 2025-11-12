package com.xaxc.teqin.model.dto;

import com.xaxc.teqin.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends Role {

    private String keyword;
}
