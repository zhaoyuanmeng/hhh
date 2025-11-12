package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.mapper.OrgMapper;
import com.xaxc.teqin.model.dto.OrgDTO;
import com.xaxc.teqin.model.entity.Org;
import com.xaxc.teqin.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class OrgServiceImpl implements IOrgService {

    @Resource
    private OrgMapper mapper;

    @Override
    public ResponseResult add(Org roleEntity) {
        OrgDTO orgDTO = mapper.selectOrgByName(roleEntity.getFullName());
        if (orgDTO != null) {
            return ResponseResult.error("机构名称已存在");
        }
        orgDTO = mapper.selectOrgByName(roleEntity.getShortName());
        if (orgDTO != null) {
            return ResponseResult.error("机构简称已存在");
        }
        return mapper.insert(roleEntity) > 0 ? ResponseResult.success() : ResponseResult.error("新增机构失败");
    }

    @Override
    public ResponseResult update(Org roleEntity) {
        List<Org> orgEntityList = mapper.selectList(new LambdaQueryWrapper<Org>().ne(Org::getId, roleEntity.getId()).and(t -> t
                .eq(Org::getFullName, roleEntity.getFullName()).or().eq(Org::getShortName, roleEntity.getShortName())));
        if (!CollectionUtils.isEmpty(orgEntityList)) {
            return ResponseResult.error("机构名称已存在");
        }
        return mapper.update(roleEntity, new LambdaQueryWrapper<Org>().eq(Org::getId, roleEntity.getId())) > 0 ?
                ResponseResult.success() : ResponseResult.error("编辑机构失败");
    }


    @Override
    public ResponseResult delete(String id) {
        Org orgEntity = new Org();
        orgEntity.setId(id);
        orgEntity.setDeleteFlag(-1);
        return mapper.update(orgEntity, new LambdaQueryWrapper<Org>().eq(Org::getId, id)) > 0 ?
                ResponseResult.success() : ResponseResult.error("删除机构失败");
    }

    @Override
    public List<OrgDTO> getList(OrgDTO orgDTO) {
        return mapper.selectOrgList(orgDTO);
    }

    @Override
    public Page<OrgDTO> getPage(Page<OrgDTO> page, OrgDTO orgDTO) {
        return mapper.selectOrgList(page, orgDTO);
    }

}
