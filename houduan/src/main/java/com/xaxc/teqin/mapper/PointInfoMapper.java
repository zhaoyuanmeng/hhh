package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.model.entity.FeaturePositionData;
import com.xaxc.teqin.model.entity.PointExtInfo;
import com.xaxc.teqin.model.entity.PointInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 点位信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
public interface PointInfoMapper extends BaseMapper<PointInfo> {

    Double getSumOfWayTotalLength(@Param(value = "childTypeId") String childTypeId);

    Double getSumOfPointTotalArea(@Param(value = "childTypeId") String childTypeId);

    Integer getPointNumOfWay(@Param(value = "typeId") String typeId, @Param(value = "childTypeId") String childTypeId);

    List<Map<String, Object>> getPointData(@Param(value = "typeId") String typeId, @Param(value = "jcxxId") String jcxxId);

    List<Map<String, Object>> getPointType(@Param(value = "typeId") String typeId, @Param(value = "jcxxTypeId") String jcxxTypeId);

    List<Map<String, Object>> getPointSpecialType(@Param(value = "typeId") String typeId, @Param(value = "jcxxTypeId") String jcxxTypeId);

    String getIdByFieldName(@Param(value = "key") String key, @Param(value = "name") String name, @Param(value = "typeId") String typeId);

    List<Map<String, String>> getPointsByName(@Param(value = "name") String name);

    Page<PointInfo> getPointInfoPage(Page<PointInfo> pointInfoPage);

    PointInfo getByName(@Param(value = "name") String name);

    List<PointExtInfo> getPointListByJcxxId(@Param(value = "jcxxId") String jcxxId);

    List<PointInfo> queryListByParentId(@Param(value = "parentTypeId") String parentTypeId);

    List<PointInfo> queryListByJcxxId(@Param(value = "jcxxId") String jcxxId);

    String getPointDataType(@Param(value = "id") String id);

    List<Map<String, String>> getFeatureTypeData(@Param(value = "id") String id);

    List<FeaturePositionData> getFeaturePosition(@Param(value = "jcxxId") String jcxxId, @Param(value = "typeId") String typeId);

}
