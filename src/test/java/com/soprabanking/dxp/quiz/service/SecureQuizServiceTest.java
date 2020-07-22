package com.soprabanking.dxp.quiz.service;

import com.soprabanking.dxp.commons.error.DxpSbsException;
import com.soprabanking.dxp.quiz.dao.QuizDao;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.soprabanking.dxp.commons.banking.security.hostuser.test.DefaultHostUserConstants.RAURU;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.RESOURCE_NOT_FOUND;
import static com.soprabanking.dxp.commons.test.ExceptionVerifiersKt.expectSbsException;
import static com.soprabanking.dxp.commons.test.SecurityTestExtensionsKt.testWithDxpContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

// This test is separated from QuizServiceTest because our documentation is step by step and uses live code snippets.
// You MUST NEVER separate related domain logic is separated classes like this.
// If you follow the quickstart INCLUDE this method in QuizServiceTest class instead
@SpringBootTest
class SecureQuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizDao quizDao;

    @BeforeEach
    void beforeEach() {
        quizDao.initTest();
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