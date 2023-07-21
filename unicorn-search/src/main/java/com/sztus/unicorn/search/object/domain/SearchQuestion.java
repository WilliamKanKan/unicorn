package com.sztus.unicorn.search.object.domain;

import lombok.Data;

import java.io.Serializable;


@Data
public class SearchQuestion implements Serializable {
    private Long id;
    private String question;
}
