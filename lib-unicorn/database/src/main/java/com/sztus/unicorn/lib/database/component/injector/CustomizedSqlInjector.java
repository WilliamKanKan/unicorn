package com.sztus.unicorn.lib.database.component.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.injector.MPJSqlInjector;
import com.sztus.unicorn.lib.database.type.method.InsertIgnore;
import com.sztus.unicorn.lib.database.type.method.InsertIgnoreBatch;
import com.sztus.unicorn.lib.database.type.method.Replace;

import java.util.List;

/**
 * @author LuffyWang
 */
public class CustomizedSqlInjector extends MPJSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        // 插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnore());
        // 批量插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnoreBatch());
        // 替换数据，如果中已经存在相同的记录，则覆盖旧数据
        methodList.add(new Replace());
        return methodList;
    }
}
