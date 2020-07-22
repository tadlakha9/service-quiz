package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.AnswerApiDto;
import com.soprabanking.dxp.quiz.model.entity.Answer;

public final class AnswerMapper {

    private AnswerMapper() {
    }

    public static AnswerApiDto toApi(Answer input) {
        return new AnswerApiDto().setLabel(input.getLabel()).setValid(input.isValid());
    }

    public static Answer toEntity(AnswerApiDto input) {
        return new Answer().setLabel(input.getLabel()).setValid(input.isValid());
    }
}
