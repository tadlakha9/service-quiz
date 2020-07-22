package com.soprabanking.dxp.quiz;

import com.soprabanking.dxp.quiz.model.api.AnswerApiDto;
import com.soprabanking.dxp.quiz.model.api.QuestionApiDto;
import com.soprabanking.dxp.quiz.model.api.QuizApiDto;
import com.soprabanking.dxp.quiz.model.entity.Question;
import com.soprabanking.dxp.quiz.model.entity.Quiz;

import java.time.Duration;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class TestDataDto {

    public static QuizApiDto javaQuizDto() {
        Quiz quiz = TestData.javaQuiz();
        return new QuizApiDto()
                .withId(quiz.getId().toString())
                .setTitle(quiz.getTitle())
                .setQuestions(asList(javaCollectionQuestionDto(), javaCollectionQuestionDto()));
    }

    public static QuestionApiDto javaCollectionQuestionDto() {
        return toApi(TestData.javaImmutableQuestion());
    }

    private static QuestionApiDto toApi(Question q) {
        return new QuestionApiDto()
                .setLabel(q.getLabel())
                .setDuration(Duration.ofSeconds(q.getDurationInSeconds()))
                .setAnswers(q.getAnswers().stream()
                             .map(a -> new AnswerApiDto().setLabel(a.getLabel()).setValid(a.isValid()))
                             .collect(toList()));
    }
}
