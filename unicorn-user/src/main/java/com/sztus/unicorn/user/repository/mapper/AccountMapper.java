package com.sztus.unicorn.user.repository.mapper;

import com.github.yulichang.base.mapper.MPJJoinMapper;
import com.sztus.unicorn.user.object.domain.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends MPJJoinMapper<Account> {
}
