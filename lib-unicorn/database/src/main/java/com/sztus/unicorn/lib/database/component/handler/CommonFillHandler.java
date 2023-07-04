package com.sztus.unicorn.lib.database.component.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sztus.unicorn.lib.core.util.DateUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author LuffyWang
 */
@Component
public class CommonFillHandler implements MetaObjectHandler {

    /**
     * 插入时要填充的字段值
     *
     * @param metaObject 元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", Long.class, DateUtil.getCurrentTimestamp());
    }

    /**
     * 更新时要填充的字段值
     *
     * @param metaObject 元数据
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", Long.class, DateUtil.getCurrentTimestamp());
    }
}
