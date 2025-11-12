package com.xaxc.teqin.model.dto;

import com.xaxc.teqin.model.entity.Org;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgDTO extends Org {

    private String keyword;
}
