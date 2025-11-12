package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 井盖信息
 * </p>
 *
 * @author author
 * @since 2025-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cover_info")
public class CoverInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String deviceName;

    private String modelType;

    private String clientId;

    private String addr;

    private String lng;

    private String lat;

    private Integer isAlarm;

    private LocalDateTime activeAt;

    private LocalDateTime lastUploadAt;

    private String status;


}
