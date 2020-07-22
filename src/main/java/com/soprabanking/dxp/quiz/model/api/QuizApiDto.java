package com.soprabanking.dxp.quiz.model.api;

import com.soprabanking.dxp.commons.constants.IdentifiableDTO;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizApiDto implements IdentifiableDTO {

    private String id;

    @NotEmpty
    private String title;

    private Duration duration;

    private List<QuestionApiDto> questions = new ArrayList<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public QuizApiDto withId(String id) {
        setId(id);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public QuizApiDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public QuizApiDto setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public List<QuestionApiDto> getQuestions() {
        return questions;
    }

    public QuizApiDto setQuestions(List<QuestionApiDto> questions) {
        this.questions = questions;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, duration, questions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizApiDto)) {
            return false;
        }
        QuizApiDto that = (QuizApiDto) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(title, that.title) &&
               Objects.equals(duration, that.duration) &&
               Objects.equals(questions, that.questions);
    }
}
