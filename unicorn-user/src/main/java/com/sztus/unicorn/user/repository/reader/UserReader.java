package com.sztus.unicorn.user.repository.reader;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import com.sztus.unicorn.user.object.domain.User;
import org.springframework.stereotype.Repository;

@Repository
@DS("reader")
public class UserReader extends MPJBaseServiceImpl<UserMapper, User> {
}
