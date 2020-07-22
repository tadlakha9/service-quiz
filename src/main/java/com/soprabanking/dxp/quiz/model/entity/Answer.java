package com.soprabanking.dxp.quiz.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class Answer {

    private String label;

    private boolean valid;

    public String getLabel() {
        return label;
    }

    public Answer setLabel(String label) {
        this.label = label;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public Answer setValid(boolean valid) {
        this.valid = valid;
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
        if (!(o instanceof Answer)) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(label, answer.label);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", label)
                .append("valid", valid)
                .toString();
    }
}
