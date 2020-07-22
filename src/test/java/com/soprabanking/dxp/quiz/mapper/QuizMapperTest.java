package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.QuizApiDto;
import com.soprabanking.dxp.quiz.model.entity.Question;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.soprabanking.dxp.commons.mongodb.ObjectIdMappingsKt.toObjectId;
import static com.soprabanking.dxp.quiz.TestData.javaQuiz;
import static com.soprabanking.dxp.quiz.TestDataDto.javaQuizDto;
import static com.soprabanking.dxp.quiz.mapper.QuizMapper.toApi;
import static com.soprabanking.dxp.quiz.mapper.QuizMapper.toEntity;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class QuizMapperTest {

    @Test
    void toApi_shouldMap() {
        Quiz input = javaQuiz();
        QuizApiDto expected = toApi(input);
        assertThat(toObjectId(expected.getId())).isEqualTo(input.getId());
        assertThat(expected.getTitle()).isEqualTo(input.getTitle());
        assertThat(expected.getQuestions()).isNotEmpty();
        assertThat(expected.getDuration())
                .isEqualTo(Duration.ofSeconds(input.getQuestions().stream().mapToLong(Question::getDurationInSeconds).sum()));
    }

    @Test
    void toEntity_shouldMap() {
        QuizApiDto dto = javaQuizDto();
        Quiz expected = toEntity(dto);
        assertThat(expected.getId()).isEqualTo(toObjectId(dto.getId()));
        assertThat(expected.getTitle()).isEqualTo(dto.getTitle());
        assertThat(expected.getQuestions()).isNotEmpty();
    }
}
