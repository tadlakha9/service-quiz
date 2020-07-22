package com.soprabanking.dxp.quiz.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {

    private String label;

    private long durationInSeconds;

    private List<Answer> answers = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public Question setLabel(String label) {
        this.label = label;
        return this;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public Question setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        return this;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Question setAnswers(List<Answer> answers) {
        this.answers = answers;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(label, question.label);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", label)
                .toString();
    }
}
