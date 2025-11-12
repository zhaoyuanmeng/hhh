package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.service.IFileInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 文件相关 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-15
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IFileInfoService fileInfoService;

    /**
     * 文件上传
     *
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file, @RequestParam("groupId") String groupId) throws IOException {
        return ResponseResult.success(fileInfoService.uploadFile(file, groupId));
    }

    /**
     * 多文件上传
     *
     * @param files
     * @param groupId
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFiles")
    public ResponseResult uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("groupId") String groupId) throws IOException {
        return ResponseResult.success(fileInfoService.uploadFiles(files, groupId));
    }

    /**
     * 下载文件
     *
     * @param fileId
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileId") String fileId, HttpServletResponse response) throws IOException {
        fileInfoService.downloadFile(fileId, response);
    }

    /**
     * 图片文件预览
     *
     * @param fileId
     * @param response
     * @throws IOException
     */
    @GetMapping("/preview")
    public void preview(@RequestParam("fileId") String fileId, HttpServletResponse response) throws IOException {
        fileInfoService.previewFile(fileId, response);
    }

    /**
     * 根据文件组ID查询所有文件ID
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @GetMapping("/getFileIds")
    public ResponseResult getFileIds(@RequestParam("groupId") String groupId) {
        return ResponseResult.success(fileInfoService.getFileIds(groupId));
    }


    /**
     * 文件上传
     *
     * @return
     */
    @PostMapping("/uploadFile")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("businessId") String businessId, @RequestParam(value = "businessType", required = false) String businessType) throws IOException {
        return ResponseResult.success(fileInfoService.uploadFile(file, businessId, businessType));
    }

    @GetMapping("/deleteFile")
    public ResponseResult deleteFile(@RequestParam("businessId") String businessId) {
        return ResponseResult.success(fileInfoService.deleteFile(businessId));
    }

}
