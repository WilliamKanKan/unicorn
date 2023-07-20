package com.sztus.unicorn.search.object.response;

import com.sztus.unicorn.search.object.domain.SearchAnswer;
import com.sztus.unicorn.search.object.domain.SearchQuestion;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnswerResponse implements Serializable {
    private Long userId;
    private Long id;
    private List<SearchQuestion> searchQuestions;
    private List<SearchAnswer> searchAnswers;

}
