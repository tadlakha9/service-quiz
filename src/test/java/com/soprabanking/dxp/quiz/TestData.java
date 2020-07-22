package com.soprabanking.dxp.quiz;

import com.soprabanking.dxp.quiz.model.entity.Answer;
import com.soprabanking.dxp.quiz.model.entity.Question;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import org.bson.types.ObjectId;

import static java.util.Arrays.asList;

public class TestData {
    public static ObjectId javaQuizId = new ObjectId(0, 0);

    public static Quiz javaQuiz() {
        return new Quiz()
                .withId(javaQuizId)
                .setTitle("Java")
                .setQuestions(asList(javaImmutableQuestion(), javaCollectionQuestion()));
    }

    public static Question javaImmutableQuestion() {
        return new Question()
                .setLabel("What is an immutable object")
                .setDurationInSeconds(45)
                .setAnswers(asList(
                        new Answer().setLabel("An immutable object can be changed once it is created"),
                        new Answer().setLabel("An immutable object can't be changed once it is created").setValid(true),
                        new Answer().setLabel("An immutable object is an instance of an abstract class"),
                        new Answer().setLabel("None of the above")
                ));
    }

    public static Question javaCollectionQuestion() {
        return new Question()
                .setLabel("Which of those classes extends the interface 'Collection' ?")
                .setDurationInSeconds(60)
                .setAnswers(asList(
                        new Answer().setLabel("List").setValid(true),
                        new Answer().setLabel("Map"),
                        new Answer().setLabel("Set").setValid(true),
                        new Answer().setLabel("Arrays")
                ));
    }
}
