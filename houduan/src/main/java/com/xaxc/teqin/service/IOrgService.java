package com.xaxc.teqin.service;


import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.OrgDTO;
import com.xaxc.teqin.model.entity.Org;

import java.util.List;

public interface IOrgService {

    ResponseResult add(Org org);

    ResponseResult update(Org org);

    ResponseResult delete(String id);

    List<OrgDTO> getList(OrgDTO orgDTO);

    Page<OrgDTO> getPage(Page<OrgDTO> page, OrgDTO orgDTO);

}
