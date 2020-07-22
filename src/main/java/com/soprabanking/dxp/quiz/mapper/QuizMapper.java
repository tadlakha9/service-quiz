package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.QuizApiDto;
import com.soprabanking.dxp.quiz.model.entity.Question;
import com.soprabanking.dxp.quiz.model.entity.Quiz;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.soprabanking.dxp.commons.mongodb.ObjectIdMappingsKt.toObjectId;

public final class QuizMapper {

    private QuizMapper() {
    }

    public static QuizApiDto toApi(Quiz input) {
        return new QuizApiDto()
                .withId(input.getId().toString())
                .setTitle(input.getTitle())
                .setQuestions(input.getQuestions().stream().map(QuestionMapper::toApi).collect(Collectors.toList()))
                .setDuration(duration(input.getQuestions()));
    }

    public static Quiz toEntity(QuizApiDto input) {
        return new Quiz()
                .withId(toObjectId(input.getId()))
                .setTitle(input.getTitle())
                .setQuestions(input.getQuestions().stream().map(QuestionMapper::toEntity).collect(Collectors.toList()));
    }

    private static Duration duration(List<Question> questions) {
        return Duration.ofSeconds(questions.stream().mapToLong(Question::getDurationInSeconds).sum());
    }
}
