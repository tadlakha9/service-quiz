package com.soprabanking.dxp.quiz.service;

import com.soprabanking.dxp.commons.error.DxpSbsException;
import com.soprabanking.dxp.quiz.config.QuestionProperties;
import com.soprabanking.dxp.quiz.dao.QuizDao;
import com.soprabanking.dxp.quiz.model.entity.Question;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.soprabanking.dxp.commons.banking.security.hostuser.test.DefaultHostUserConstants.RAURU;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.INVALID_CONTENT;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.RESOURCE_ALREADY_EXIST;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.RESOURCE_NOT_FOUND;
import static com.soprabanking.dxp.commons.page.RangeConstants.DEFAULT_RANGE;
import static com.soprabanking.dxp.commons.test.ExceptionVerifiersKt.expectSbsException;
import static com.soprabanking.dxp.commons.test.SecurityTestExtensionsKt.testWithDxpContext;
import static com.soprabanking.dxp.quiz.TestData.javaCollectionQuestion;
import static com.soprabanking.dxp.quiz.TestData.javaQuiz;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static reactor.kotlin.test.StepVerifierExtensionsKt.test;

@SpringBootTest
class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionProperties questionProperties;

    @BeforeEach
    void beforeEach() {
        quizDao.initTest();
    }

    @Test
    void findAll_shouldFindSome() {
        test(quizService.findAll(DEFAULT_RANGE))
                .expectSubscription()
                .expectNextCount(quizDao.count())
                .verifyComplete();
    }

    @Test
    void findAll_shouldNotFindAny() {
        quizDao.deleteAll();
        test(quizService.findAll(DEFAULT_RANGE))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void create_shouldPersistOne() {
        Quiz quiz = new Quiz().setTitle("Advanced Java").setQuestions(Collections.singletonList(javaCollectionQuestion()));
        test(quizService.create(quiz))
                .expectSubscription()
                .consumeNextWith(q -> assertThat(quizDao.findById(q.getId())).isEqualTo(quiz))
                .verifyComplete();
    }

    @Test
    void create_shouldFailWhenAlreadyExists() {
        test(quizService.create(new Quiz().setTitle(javaQuiz().getTitle())))
                .expectSubscription()
                .consumeErrorWith(e -> expectSbsException(e, RESOURCE_ALREADY_EXIST))
                .verify();
    }

    @Test
    void create_shouldFailWhenTooManyQuestions() {
        List<Question> questions = IntStream.rangeClosed(0, questionProperties.getMax()).boxed()
                                            .map(i -> javaCollectionQuestion())
                                            .collect(toList());
        Quiz quiz = quizDao.findAny().setQuestions(questions);
        test(quizService.create(quiz))
                .expectSubscription()
                .consumeErrorWith(e -> expectSbsException(e, INVALID_CONTENT))
                .verify();
    }


    @Test
    void deleteById_shouldSucceedForDepAdministrator() {
        Quiz quiz = quizDao.findAny();
        testWithDxpContext(quizService.deleteById(quiz.getId()), RAURU)
                .expectSubscription()
                .expectNext(quiz)
                .then(() -> assertThatExceptionOfType(DxpSbsException.class).isThrownBy(() -> quizDao.findById(quiz.getId())))
                .verifyComplete();
    }

    @Test
    void deleteById_shouldFailWhenNotFound() {
        testWithDxpContext(quizService.deleteById(new ObjectId()))
                .expectSubscription()
                .consumeErrorWith(e -> expectSbsException(e, RESOURCE_NOT_FOUND))
                .verify();
    }

}
