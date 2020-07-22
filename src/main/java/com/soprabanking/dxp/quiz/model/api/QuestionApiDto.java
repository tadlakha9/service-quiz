package com.soprabanking.dxp.quiz.model.api;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionApiDto {

    @NotNull
    private String label;

    private Duration duration;

    private List<AnswerApiDto> answers = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public QuestionApiDto setLabel(String label) {
        this.label = label;
        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public QuestionApiDto setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public List<AnswerApiDto> getAnswers() {
        return answers;
    }

    public QuestionApiDto setAnswers(List<AnswerApiDto> answers) {
        this.answers = answers;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, duration, answers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionApiDto)) {
            return false;
        }
        QuestionApiDto that = (QuestionApiDto) o;
        return Objects.equals(label, that.label) &&
               Objects.equals(duration, that.duration) &&
               Objects.equals(answers, that.answers);
    }
}
