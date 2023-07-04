package com.sztus.unicorn.lib.database.type.enumerate;

import lombok.Getter;

/**
 * MybatisPlus自定义SQL方法枚举
 * @author LuffyWang
 */
@Getter
public enum SqlMethodEnum {
    /**
     * 插入
     */
    INSERT_IGNORE_ONE("insertIgnore", "Insert a piece of data (select field to insert), if the same record already exists in, ignore the current new data", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>"),
    /**
     * 替换
     */
    REPLACE_ONE("replace", "Replace a piece of data (select field to insert), replace if it exists, and insert if it does not exist", "<script>\nREPLACE INTO %s %s VALUES %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    SqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }
}

