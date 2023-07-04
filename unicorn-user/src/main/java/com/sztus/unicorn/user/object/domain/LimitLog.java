package com.sztus.unicorn.user.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "limit_log")
public class LimitLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer amount;
    private Long createdAt;
}
