package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author author
 * @since 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file_info")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 文件组ID
     */
    @TableField("group_id")
    private String groupId;


    @TableField("business_id")
    private String businessId;


    /**
     * 原始文件名
     */
    @TableField("original_file_name")
    private String originalFileName;

    /**
     * 新文件名
     */
    @TableField("new_file_name")
    private String newFileName;

    /**
     * 文件扩展名
     */
    @TableField("file_ext")
    private String fileExt;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    /**
     * 创建用户
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;


}
