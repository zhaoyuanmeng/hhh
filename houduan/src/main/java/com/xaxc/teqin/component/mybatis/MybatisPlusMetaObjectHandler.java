package com.xaxc.teqin.component.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Automatic filling for set values
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * Automatic filling when inserting into the database.
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.systemDefault()));
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.systemDefault()));

        this.strictInsertFill(metaObject, "deleteFlag", Integer.class,
                0);
    }

    /**
     * Automatic filling when updating the data.
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.systemDefault()));
    }
}
