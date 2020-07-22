package com.soprabanking.dxp.quiz.dao;

import com.soprabanking.dxp.commons.test.MongoDao;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.soprabanking.dxp.quiz.TestData.javaQuiz;
import static java.util.Arrays.asList;

@Component
public class QuizDao extends MongoDao<Quiz> {

    public QuizDao(MongoTemplate mongoTemplate) {
        super(Quiz.class, mongoTemplate);
    }

    @Override
    public List<Quiz> defaultEntities() {
        return asList(
                javaQuiz(),
                javaQuiz().withId(new ObjectId(0, 1)).setTitle("Kotlin")
        );
    }
}