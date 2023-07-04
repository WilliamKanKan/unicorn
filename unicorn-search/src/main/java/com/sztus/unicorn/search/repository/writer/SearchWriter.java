package com.sztus.unicorn.search.repository.writer;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.repository.mapper.SearchMapper;

import org.springframework.stereotype.Repository;

/**
 * @author LuffyWang
 */
@Repository
@DS("writer")
public class SearchWriter extends MPJBaseServiceImpl<SearchMapper, SearchLog> {

}
