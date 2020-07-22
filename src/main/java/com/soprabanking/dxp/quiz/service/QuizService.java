package com.soprabanking.dxp.quiz.service;

import com.soprabanking.dxp.commons.error.DxpBusinessCode;
import com.soprabanking.dxp.commons.error.DxpSbsException;
import com.soprabanking.dxp.commons.error.DxpSbsFeedback;
import com.soprabanking.dxp.commons.mongodb.error.MongoErrorMappingsKt;
import com.soprabanking.dxp.commons.page.Range;
import com.soprabanking.dxp.commons.security.model.DxpAuthContext;
import com.soprabanking.dxp.quiz.config.QuestionProperties;
import com.soprabanking.dxp.quiz.model.entity.Quiz;
import com.soprabanking.dxp.quiz.repository.QuizRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.soprabanking.dxp.commons.security.ReactiveSecurityContextKt.currentDxpAuthContext;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.RESOURCE_NOT_FOUND;

import static java.lang.String.format;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;

    private final QuestionProperties questionProperties;

    public QuizService(QuizRepository quizRepository, QuestionProperties questionProperties) {
        this.quizRepository = quizRepository;
        this.questionProperties = questionProperties;
    }

    public Flux<Quiz> findAll(Range range) {
        return quizRepository.findAll(range)
                             .doOnSubscribe(s -> logger.debug("Searching quizzes within {}", range))
                             .doOnComplete(() -> logger.debug("Quizzes within {} retrieved", range));
    }

    public Mono<Quiz> create(Quiz quiz) {
        return Mono.fromSupplier(() -> checkQuiz(quiz))
                   .flatMap(quizRepository::insert)
                   .doOnSubscribe(s -> logger.info("Creating {}", quiz))
                   .doOnNext(q -> logger.info("Created {}", q))
                   .onErrorMap(MongoErrorMappingsKt::mapSbsMongoError);
    }

    private Quiz checkQuiz(Quiz quiz) {
        if (quiz.getQuestions().size() > questionProperties.getMax()) {
            throw tooManyQuestionsError(quiz);
        }
        return quiz;
    }

    private DxpSbsException tooManyQuestionsError(Quiz quiz) {
        String msg = format("Quiz %s has more questions than maximum authorized %s", quiz.getTitle(), questionProperties.getMax());
        return new DxpSbsFeedback.Builder(msg, DxpBusinessCode.INVALID_CONTENT, logger::debug)
                .withSource(format("%s.questions#size", Quiz.class.getSimpleName()))
                .build()
                .toException();
    }

    public Mono<Quiz> deleteById(ObjectId id) {
        return quizRepository.findById(id)
                             .switchIfEmpty(Mono.error(notFoundError(id)))
                             .flatMap(this::delete)
                             .doOnSubscribe(s -> logger.info("Deleting quiz {}", id));

    }

    private Mono<Quiz> delete(Quiz quiz) {
        return Mono.justOrEmpty(quiz.getId())
                   .flatMap(quizRepository::deleteById)
                   .then(logOnDeleted(quiz));

    }

    private Mono<Quiz> logOnDeleted(Quiz quiz) {
        return currentDxpAuthContext()
                .map(DxpAuthContext::getClientId)
                .defaultIfEmpty("unknown")
                .doOnNext(u -> logger.info("Quiz {} deleted by {}", quiz.getId(), u))
                .thenReturn(quiz);

    }

    private DxpSbsException notFoundError(ObjectId id) {
        return new DxpSbsFeedback(format("Quiz %s not found", id), RESOURCE_NOT_FOUND, logger::info).toException();
    }
}



