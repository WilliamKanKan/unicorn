package com.sztus.unicorn.lib.database.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.unicorn.lib.database.mapper.CustomizedBaseMapper;

import java.util.List;

/**
 * 此处可以写通用的Service
 *
 * @param <M>
 * @param <T>
 * @author LuffyWang
 */
public class CustomizeServiceImpl<M extends CustomizedBaseMapper<T>, T> extends ServiceImpl<M, T> implements CustomizeService<T> {

    @Override
    public int insertIgnore(T entity) {
        return baseMapper.insertIgnore(entity);
    }

    @Override
    public int insertIgnoreBatch(List<T> entityList) {
        return baseMapper.insertIgnoreBatch(entityList);
    }

    @Override
    public int replace(T entity) {
        return baseMapper.replace(entity);
    }

    @Override
    public Class<T> currentModelClass() {
        return super.currentModelClass();
    }
}

