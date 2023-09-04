package com.sztus.unicorn.search.object.response;

import com.sztus.unicorn.search.object.domain.SearchAnswer;
import lombok.Data;
import java.io.Serializable;


@Data
public class AnswerResponse implements Serializable {
    private Long logId;
    private SearchAnswer searchAnswer;

}
