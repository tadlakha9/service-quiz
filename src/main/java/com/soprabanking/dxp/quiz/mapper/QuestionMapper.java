package com.soprabanking.dxp.quiz.mapper;

import com.soprabanking.dxp.quiz.model.api.QuestionApiDto;
import com.soprabanking.dxp.quiz.model.entity.Question;

import java.time.Duration;

import static java.util.stream.Collectors.toList;

public final class QuestionMapper {

    private QuestionMapper() {
    }

    public static QuestionApiDto toApi(Question input) {
        return new QuestionApiDto()
                .setLabel(input.getLabel())
                .setDuration(Duration.ofSeconds(input.getDurationInSeconds()))
                .setAnswers(input.getAnswers().stream().map(AnswerMapper::toApi).collect(toList()));
    }

    public static Question toEntity(QuestionApiDto input) {
        return new Question()
                .setLabel(input.getLabel())
                .setDurationInSeconds(mapDuration(input.getDuration()))
                .setAnswers(input.getAnswers().stream().map(AnswerMapper::toEntity).collect(toList()));
    }

    private static long mapDuration(Duration duration) {
        return duration != null ? duration.getSeconds() : 30;
    }
}
