package com.sztus.unicorn.search.object.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchAnswer implements Serializable {
    private Long id;
    private String answer;
}
