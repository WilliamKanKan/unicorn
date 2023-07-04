package com.sztus.unicorn.user.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@TableName(value = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private String password;
    private String salt;
    private String token;
    private Long expiredAt;

}
