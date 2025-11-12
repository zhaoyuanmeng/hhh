package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.model.entity.IssuesData;
import com.xaxc.teqin.mapper.IssuesDataMapper;
import com.xaxc.teqin.service.IIssuesDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-14
 */
@Service
public class IssuesDataServiceImpl extends ServiceImpl<IssuesDataMapper, IssuesData> implements IIssuesDataService {

    @Override
    public List<IssuesData> getList(String id) {
        LambdaQueryWrapper<IssuesData> lambdaQueryWrapper = new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, "");
        if (StringUtils.hasText(id)) {
            lambdaQueryWrapper.eq(IssuesData::getId, id);
        }
        lambdaQueryWrapper.orderByAsc(IssuesData::getCreateTime);
        List<IssuesData> first = list(lambdaQueryWrapper);
        for (IssuesData issuesData : first) {
            int total = 0;
            List<IssuesData> second = list(new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, issuesData.getId()).orderByAsc(IssuesData::getCreateTime));
            for (IssuesData data : second) {
                List<IssuesData> third = list(new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, data.getId()).orderByAsc(IssuesData::getCreateTime));
                if (CollectionUtils.isEmpty(third)) {
                    total++;
                } else {
                    third.forEach(issues -> issues.setLevel(3));
                    data.setChildren(third);
                    data.setTotal(third.size());
                    total += third.size();
                }
                data.setLevel(2);
            }
            issuesData.setTotal(total);
            issuesData.setChildren(second);
            issuesData.setLevel(1);
        }
        return first;
    }

    @Override
    public int getIssuesNum() {
        int total = 0;
        List<IssuesData> first = list(new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, "").orderByAsc(IssuesData::getCreateTime));
        for (IssuesData issuesData : first) {
            List<IssuesData> second = list(new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, issuesData.getId()).orderByAsc(IssuesData::getCreateTime));
            for (IssuesData data : second) {
                List<IssuesData> third = list(new LambdaQueryWrapper<IssuesData>().eq(IssuesData::getDeleteFlag, "0").eq(IssuesData::getParentId, data.getId()).orderByAsc(IssuesData::getCreateTime));
                if (CollectionUtils.isEmpty(third)) {
                    total++;
                } else {
                    total += third.size();
                }
            }
            if (CollectionUtils.isEmpty(second)) {
                total++;
            }
        }
        return total;
    }
}
