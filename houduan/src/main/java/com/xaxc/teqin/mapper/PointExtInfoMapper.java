package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.model.entity.PointExtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-08-26
 */
public interface PointExtInfoMapper extends BaseMapper<PointExtInfo> {
    List<PointExtInfo> queryIntersectsData(String polygon);

    List<PointExtInfo> queryIntersectsDataOfWay(@Param("polygon")String polygon,@Param("jcxxId")String jcxxId);

    List<PointExtInfo> getPointExtInfoList(@Param("ids") List<String> ids);


    List<PointExtInfo> queryIntersectsPointData(@Param("polygon")String polygon,@Param("line")String line);
}
