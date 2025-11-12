package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.model.dto.OrgDTO;
import com.xaxc.teqin.model.entity.Org;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgMapper extends BaseMapper<Org> {


    OrgDTO selectOrgByName(String keyword);

    List<OrgDTO> selectOrgList(@Param("dto") OrgDTO orgDTO);


    Page<OrgDTO> selectOrgList(Page page, @Param("dto") OrgDTO orgDTO);

}
