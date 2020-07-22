package com.soprabanking.dxp.quiz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Component
@Validated
@ConfigurationProperties("questions")
public class QuestionProperties {

    @Positive
    private int max = 20;

    public int getMax() {
        return max;
    }

    public QuestionProperties setMax(int max) {
        this.max = max;
        return this;
    }
}
