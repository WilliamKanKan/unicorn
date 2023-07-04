package com.sztus.unicorn.user.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value = "user_limit")
public class UserLimit {
    @TableId(value = "id",type = IdType.AUTO)

    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "limit_value")
    private Integer limitValue;
}

