package com.soprabanking.dxp.quiz.model.api;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AnswerApiDto {

    @NotNull
    private String label;

    private boolean valid;

    public String getLabel() {
        return label;
    }

    public AnswerApiDto setLabel(String label) {
        this.label = label;
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public AnswerApiDto setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, valid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnswerApiDto)) {
            return false;
        }
        AnswerApiDto that = (AnswerApiDto) o;
        return valid == that.valid &&
               Objects.equals(label, that.label);
    }
}
