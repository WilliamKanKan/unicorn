package com.sztus.unicorn.search.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "search_log")
public class SearchLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String question;
    private Long createdAt;
}