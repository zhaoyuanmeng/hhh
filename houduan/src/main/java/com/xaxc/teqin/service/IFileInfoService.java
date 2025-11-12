package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.FileInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文件信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-18
 */
public interface IFileInfoService extends IService<FileInfo> {

    boolean deleteFile(String businessId);

    boolean uploadFile(MultipartFile file, String businessId, String businessType) throws IOException;

    String uploadFile(MultipartFile file, String groupId) throws IOException;

    String uploadFiles(MultipartFile[] files, String groupId) throws IOException;

    void downloadFile(String fileId, HttpServletResponse response) throws IOException;

    void previewFile(String fileId, HttpServletResponse response) throws IOException;

    List<String> getFileIds(String groupId);

    String uploadFile(MultipartFile file) throws IOException;

    void uploadPointFile(MultipartFile file);

    File transferToFile(MultipartFile multipartFile, String outFilePath);

    List<String> splitAndReplaceInFile(File inputFile, String outputPath, List<String> targets, String replacement, long splitLineNum);


    String uploadFileAndGetFilePath(MultipartFile file) throws IOException;

}
