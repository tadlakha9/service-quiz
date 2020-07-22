package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.QuestionApiDto;
import com.soprabanking.dxp.quiz.model.entity.Question;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.soprabanking.dxp.quiz.TestData.javaCollectionQuestion;
import static com.soprabanking.dxp.quiz.TestDataDto.javaCollectionQuestionDto;
import static com.soprabanking.dxp.quiz.mapper.QuestionMapper.toApi;
import static com.soprabanking.dxp.quiz.mapper.QuestionMapper.toEntity;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class QuestionMapperTest {

    @Test
    void toApi_shouldMap() {
        Question input = javaCollectionQuestion();
        QuestionApiDto expected = toApi(input);
        assertThat(expected.getLabel()).isEqualTo(input.getLabel());
        assertThat(expected.getDuration()).isEqualTo(Duration.ofSeconds(input.getDurationInSeconds()));
        assertThat(expected.getAnswers()).isNotEmpty();
    }

    @Test
    void toEntity_shouldMap() {
        QuestionApiDto input = javaCollectionQuestionDto();
        Question expected = toEntity(input);
        assertThat(expected.getLabel()).isEqualTo(input.getLabel());
        assertThat(expected.getDurationInSeconds()).isEqualTo(input.getDuration().getSeconds());
        assertThat(expected.getAnswers()).isNotEmpty();
    }

    @Test
    void toEntity_shouldDefaultDurationTo30SecondsWhenNull() {
        QuestionApiDto input = javaCollectionQuestionDto().setDuration(null);
        Question expected = toEntity(input);
        assertThat(expected.getDurationInSeconds()).isEqualTo(30);
    }

    @Test
    void toEntityWithNullDuration_shouldDefaultTo30() {
        assertThat(toEntity(new QuestionApiDto()).getDurationInSeconds()).isEqualTo(30);
    }
}
