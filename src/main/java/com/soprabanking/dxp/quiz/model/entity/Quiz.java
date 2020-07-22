package com.soprabanking.dxp.quiz.model.entity;

import com.soprabanking.dxp.commons.constants.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Quiz implements Identifiable<ObjectId> {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String title;

    private List<Question> questions = new ArrayList<>();

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public Quiz withId(ObjectId id) {
        setId(id);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Quiz setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Quiz setQuestions(List<Question> questions) {
        this.questions = questions;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        Quiz quiz = (Quiz) o;
        return Objects.equals(title, quiz.title);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .toString();
    }
}
