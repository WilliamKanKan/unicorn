package com.sztus.unicorn.user.repository.writer;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.sztus.unicorn.user.object.domain.User;
import com.sztus.unicorn.user.repository.mapper.UserMapper;
import org.springframework.stereotype.Repository;

/**
 * @author LuffyWang
 */
@Repository
@DS("writer")
public class UserWriter extends MPJBaseServiceImpl<UserMapper, User> {

}
