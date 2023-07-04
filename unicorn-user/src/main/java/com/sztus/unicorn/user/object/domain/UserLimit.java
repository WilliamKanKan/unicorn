package com.sztus.unicorn.user.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "user_limit")
public class UserLimit implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)

    private Long id;
    private Long userId;
    private Integer limitValue;
}

