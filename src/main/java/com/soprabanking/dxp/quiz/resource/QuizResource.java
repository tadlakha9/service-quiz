package com.soprabanking.dxp.quiz.resource;

import com.soprabanking.dxp.commons.banking.security.hostuser.model.DxpHostUserBusinessContext;
import com.soprabanking.dxp.commons.page.Range;
import com.soprabanking.dxp.commons.security.TypeAllowed;
import com.soprabanking.dxp.quiz.mapper.QuizMapper;
import com.soprabanking.dxp.quiz.model.api.QuizApiDto;
import com.soprabanking.dxp.quiz.service.QuizService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static com.soprabanking.dxp.commons.api.ApiConstants.ID_PATH;
import static com.soprabanking.dxp.commons.api.pagination.RangeResponseKt.toResponse;
import static com.soprabanking.dxp.quiz.resource.ResourceConstants.QUIZZES;

@RestController
@RequestMapping(QUIZZES)
class QuizResource {

    private final QuizService quizService;

    public QuizResource(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<QuizApiDto>>> findAll(Range range) {
        return quizService.findAll(range)
                          .map(QuizMapper::toApi)
                          .as(f -> toResponse(f, QuizApiDto.class, range));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<QuizApiDto> create(@RequestBody @Valid QuizApiDto quiz) {
        return quizService.create(QuizMapper.toEntity(quiz))
                          .map(QuizMapper::toApi);
    }

    @DeleteMapping(ID_PATH)
    @TypeAllowed(DxpHostUserBusinessContext.class)
    public Mono<QuizApiDto> deleteById(@PathVariable ObjectId id) {
        return quizService.deleteById(id).map(QuizMapper::toApi);
    }

}
