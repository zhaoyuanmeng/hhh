package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("issues_data")
public class IssuesData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("parent_id")
    private String parentId;

    @TableField("content")
    private String content;

    @TableField("delete_flag")
    @TableLogic(value = "0", delval = "-1")
    private String deleteFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private int total;

    @TableField(exist = false)
    private int level;

    @TableField(exist = false)
    private List<IssuesData> children;


}
