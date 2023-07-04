package com.sztus.unicorn.user.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@TableName("user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String openId;
    @TableField(exist = false)
    private String password;
    @TableField(exist = false)
    private String confirmPassword;
}
