package com.xaxc.teqin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.mapper.PointInfoMapper;
import com.xaxc.teqin.mapper.SpecialServiceArchivesFormMapper;
import com.xaxc.teqin.mapper.SpecialServiceArchivesMapper;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.model.entity.SpecialServiceArchives;
import com.xaxc.teqin.model.entity.SpecialServiceArchivesForm;
import com.xaxc.teqin.service.ISpecialServiceArchivesFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 档案自定义表单项信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Service
public class SpecialServiceArchivesFormServiceImpl extends ServiceImpl<SpecialServiceArchivesFormMapper, SpecialServiceArchivesForm> implements ISpecialServiceArchivesFormService {

    @Resource
    SpecialServiceArchivesMapper specialServiceArchivesMapper;

    @Resource
    PointInfoMapper pointInfoMapper;

    @Override
    public List<SpecialServiceArchivesForm> getKV(String archivesId) {
        return list(new LambdaQueryWrapper<SpecialServiceArchivesForm>().select(SpecialServiceArchivesForm::getFieldId).select(SpecialServiceArchivesForm::getFieldName).eq(SpecialServiceArchivesForm::getArchivesId, archivesId));
    }

    @Override
    public List<SpecialServiceArchivesForm> selectPointInfo(String id) {
        return list(new LambdaQueryWrapper<SpecialServiceArchivesForm>()
                .eq(SpecialServiceArchivesForm::getArchivesId, id)
                .eq(SpecialServiceArchivesForm::getDelFlag, "0")
                .orderByAsc(List.of(SpecialServiceArchivesForm::getSort, SpecialServiceArchivesForm::getCreateTime)));
    }

    @Override
    public ResponseResult selectPointList(String id, String jcxxId) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(jcxxId)) {
            List<Map<String, Object>> pointInfo = specialServiceArchivesMapper.selectMaps(new LambdaQueryWrapper<SpecialServiceArchives>()
                    .ne(SpecialServiceArchives::getParentId, "0")
                    .eq(SpecialServiceArchives::getDelFlag, "0")
                    .ne(SpecialServiceArchives::getSort, "1"));
            return ResponseResult.success(pointInfo);
        }
        // 获取到 重点点位的数据
        List<PointInfo> lists = pointInfoMapper.selectList(new LambdaQueryWrapper<PointInfo>()
                .eq(PointInfo::getChildTypeId, id)
                .eq(PointInfo::getJcxxId, jcxxId));
        lists.forEach(ls -> {
            if (ls.getData() != null) {
                JSONObject data = ls.getData();
                if (data.get("video") == null) {
                    data.put("video", new ArrayList());
                }
                if (data.get("photo") == null) {
                    data.put("photo", new ArrayList());
                }
                //ls.put("data",JSONObject.parseObject(data.toString()));
            }
        });
        // 获取重点点位的动态表单
        List<SpecialServiceArchivesForm> list = selectPointInfo(id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("point", lists);

        List<JSONObject> infolist = new ArrayList<>();
        for (SpecialServiceArchivesForm info : list) {
            JSONObject vo = new JSONObject();
            if ("家庭主要成员".equals(info.getFieldName())) {
                List<Map<String, Object>> listmap = new ArrayList<>();
                Map<String, String> fm = new HashMap<>();
                fm.put("xingming", "姓名");
                fm.put("guanxi", "关系");
                fm.put("nianling", "年龄");
                fm.put("zhengzhimianmao", "政治面貌");
                fm.put("gongzuodanwei", "工作单位");
                Set<String> keySet = fm.keySet();
                int a = 0;
                for (String key : keySet) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("field_name", fm.get(key));
                    map.put("field_id", key);
                    map.put("mandatory", "1");
                    map.put("sort", a);
                    map.put("field_type", "input");
                    listmap.add(map);
                    a++;
                }
                vo.put("listFild", listmap);
            }
            vo.put("archives_id", info.getArchivesId());
            vo.put("archives_parent_id", info.getArchivesParentId());
            vo.put("create_time", info.getCreateTime());
            vo.put("create_time", info.getCreateTime());
            vo.put("default_value", info.getDefaultValue());
            vo.put("del_flag", info.getDelFlag());
            vo.put("field_id", info.getFieldId());
            vo.put("id", info.getId());
            vo.put("remark", info.getRemark());
            vo.put("update_time", info.getUpdateTime());
            vo.put("field_name", info.getFieldName());
            vo.put("mandatory", info.getMandatory());
            vo.put("sort", info.getSort());
            vo.put("field_type", info.getFieldType());
            infolist.add(vo);
        }
        hashMap.put("info", infolist);
        return ResponseResult.success(hashMap);
    }

}
