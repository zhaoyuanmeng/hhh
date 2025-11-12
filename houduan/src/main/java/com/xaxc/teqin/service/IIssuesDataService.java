package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.IssuesData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-14
 */
public interface IIssuesDataService extends IService<IssuesData> {

   List<IssuesData> getList(String id);


   int getIssuesNum();
}
