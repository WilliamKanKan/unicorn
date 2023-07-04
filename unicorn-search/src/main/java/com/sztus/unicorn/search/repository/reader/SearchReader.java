package com.sztus.unicorn.search.repository.reader;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.sztus.unicorn.search.object.domain.SearchLog;
import com.sztus.unicorn.search.repository.mapper.SearchMapper;
import org.springframework.stereotype.Repository;

@Repository
@DS("reader")
public class SearchReader extends MPJBaseServiceImpl<SearchMapper, SearchLog> {

}
