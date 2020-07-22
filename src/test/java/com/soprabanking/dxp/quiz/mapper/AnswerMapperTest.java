package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.AnswerApiDto;
import com.soprabanking.dxp.quiz.model.entity.Answer;
import org.junit.jupiter.api.Test;

import static com.soprabanking.dxp.quiz.mapper.AnswerMapper.toApi;
import static com.soprabanking.dxp.quiz.mapper.AnswerMapper.toEntity;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AnswerMapperTest {

    @Test
    void toApi_shouldMap() {
        Answer input = new Answer().setLabel("label").setValid(true);
        AnswerApiDto expected = toApi(input);
        assertThat(expected.getLabel()).isEqualTo(input.getLabel());
        assertThat(expected.isValid()).isEqualTo(input.isValid());
    }

    @Test
    void toEntity_shouldMap() {
        AnswerApiDto input = new AnswerApiDto().setLabel("label").setValid(true);
        Answer expected = toEntity(input);
        assertThat(expected.getLabel()).isEqualTo(input.getLabel());
        assertThat(expected.isValid()).isEqualTo(input.isValid());
    }
}