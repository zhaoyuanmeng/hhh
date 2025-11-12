package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.TaskArchivesData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-04-01
 */
public interface ITaskArchivesDataService extends IService<TaskArchivesData> {

    void parseToDb(String filePath, String businessId) throws IOException ;

    List<TaskArchivesData> getArchivesData(String businessId);


}
