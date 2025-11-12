package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xaxc.teqin.common.model.BaseEntity;
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
 * @since 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("import_records")
public class ImportRecords extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String updateName;


    private String updateResult;


}
