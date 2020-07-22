package com.soprabanking.dxp.quiz.resource;

import com.soprabanking.dxp.commons.error.SbsExceptionDTO;
import com.soprabanking.dxp.quiz.dao.QuizDao;
import com.soprabanking.dxp.quiz.mapper.QuizMapper;
import com.soprabanking.dxp.quiz.model.api.QuizApiDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.soprabanking.dxp.commons.banking.security.hostuser.test.DefaultHostUserConstants.HOST_USER_DEFAULT_USERNAME;
import static com.soprabanking.dxp.commons.banking.security.tenantuser.test.DefaultTenantUserConstants.TENANT_USER_DEFAULT_USERNAME;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.INVALID_CONTENT;
import static com.soprabanking.dxp.commons.error.DxpBusinessCode.RESOURCE_ALREADY_EXIST;
import static com.soprabanking.dxp.commons.page.RangeConstants.RANGE_PARAM;
import static com.soprabanking.dxp.commons.test.ExceptionVerifiersKt.verifySbsExceptionDTO;
import static com.soprabanking.dxp.dok.webtestclient.AutoDokExtensionsKt.autoDok;
import static com.soprabanking.dxp.dok.webtestclient.WebTestClientExtensionsKt.dokError;
import static com.soprabanking.dxp.dok.webtestclient.WebTestClientUriExtensionsKt.dokUri;
import static com.soprabanking.dxp.quiz.TestDataDto.javaCollectionQuestionDto;
import static com.soprabanking.dxp.quiz.TestDataDto.javaQuizDto;
import static com.soprabanking.dxp.quiz.resource.DokKt.apiDok;
import static com.soprabanking.dxp.quiz.mapper.QuizMapper.toApi;
import static com.soprabanking.dxp.quiz.resource.ResourceConstants.QUIZZES;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.contract.spec.internal.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static com.soprabanking.dxp.commons.error.DxpSecurityCode.INSUFFICIENT_PERMISSION;
// @WithMockUser is used here because at this point in the quickstart we haven't introduced security yet
// but normally all your endpoints should expect authentication.
// It is however strongly discouraged to use @WithMockUser since it causes disastrous performances.
@WithMockUser
@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
class QuizResourceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private QuizDao quizDao;

    @BeforeEach
    void setup() {
        quizDao.initTest();
        // For document part
       apiDok();
    }

    @Test
    void findAll_shouldReturnAnArrayOfQuizzes() {
        webTestClient.get()
                     .uri(QUIZZES)
                     .exchange()
                     .expectStatus().isEqualTo(PARTIAL_CONTENT)
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBodyList(QuizApiDto.class)
                     .consumeWith(res -> {
                         List<QuizApiDto> dtos = res.getResponseBody();
                         List<QuizApiDto> expected = quizDao.findAll().stream().map(QuizMapper::toApi).collect(toList());
                         assertThat(dtos).hasSize(quizDao.count()).containsExactlyInAnyOrderElementsOf(expected);
                     })
                     // for document my API chapter only
                     .consumeWith(autoDok());
    }

    @Test
    void findAll_shouldReturnSingleQuiz() {
        webTestClient.get()
                     .uri(dokUri(it -> it.path(QUIZZES).query(RANGE_PARAM, "0-0")))
                     .exchange()
                     .expectStatus().isEqualTo(PARTIAL_CONTENT)
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBodyList(QuizApiDto.class)
                     .hasSize(1);
    }

    @Test
    void findAll_shouldReturnEmptyArray() {
        quizDao.deleteAll();
        webTestClient.get()
                     .uri(QUIZZES)
                     .exchange()
                     .expectStatus().isEqualTo(PARTIAL_CONTENT)
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBodyList(QuizApiDto.class)
                     .hasSize(0);
    }

    @Test
    void create_shouldCreateAnInitializedQuiz() {
        QuizApiDto dto = new QuizApiDto().setTitle("API").setQuestions(singletonList(javaCollectionQuestionDto()));
        webTestClient.post()
                     .uri(QUIZZES)
                     .bodyValue(dto)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody(QuizApiDto.class)
                     .consumeWith(res -> {
                         QuizApiDto mapped = res.getResponseBody();
                         assertThat(mapped.getId()).isNotNull();
                         assertThat(mapped.getTitle()).isEqualTo(dto.getTitle());
                         assertThat(mapped.getQuestions()).containsExactlyInAnyOrderElementsOf(dto.getQuestions());
                         assertThat(mapped.getDuration()).isNotNull();
                     })
                     // for document my API chapter only
                     .consumeWith(autoDok());
    }

    @Test
    void create_shouldFail_whenTitleAlreadyTaken() {
        QuizApiDto dto = new QuizApiDto()
                .setTitle(javaQuizDto().getTitle())
                .setQuestions(singletonList(javaCollectionQuestionDto()));
        webTestClient.post()
                     .uri(QUIZZES)
                     .bodyValue(dto)
                     .exchange()
                     .expectStatus().isEqualTo(CONFLICT)
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody(SbsExceptionDTO.class)
                     .consumeWith(res -> verifySbsExceptionDTO(res, RESOURCE_ALREADY_EXIST))
                     // for document my API chapter only
                     .consumeWith(dokError("Already exists", singletonList("quiz")));
    }

    @Test
    void create_shouldFail_whenBlankTitle() {
        QuizApiDto dto = new QuizApiDto().setTitle("");
        webTestClient.post()
                     .uri(QUIZZES)
                     .bodyValue(dto)
                     .exchange()
                     .expectStatus().isBadRequest()
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody(SbsExceptionDTO.class)
                     .consumeWith(res -> verifySbsExceptionDTO(res, INVALID_CONTENT))
                     // for document my API chapter only
                     .consumeWith(dokError("Invalid blank title", singletonList("quiz")));
    }


    @Test
    void deleteById_shouldSucceedForAdministrator() {
        QuizApiDto quiz = toApi(quizDao.findAny());
        webTestClient.delete()
                     .uri(QUIZZES + "/{id}", quiz.getId())
                     .header(AUTHORIZATION, "Bearer " + HOST_USER_DEFAULT_USERNAME)
                     .exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody(QuizApiDto.class)
                     .isEqualTo(quiz);
    }

    @Test
    void deleteById_shouldFail_whenUnauthorized() {
        String id = toApi(quizDao.findAny()).getId();
        webTestClient.delete()
                     .uri(QUIZZES + "/{id}", id)
                     .header(AUTHORIZATION, "Bearer " + TENANT_USER_DEFAULT_USERNAME)
                     .exchange()
                     .expectStatus().isForbidden()
                     .expectHeader().contentType(APPLICATION_JSON)
                     .expectBody(SbsExceptionDTO.class)
                     .consumeWith(res -> verifySbsExceptionDTO(res, INSUFFICIENT_PERMISSION));
    }
}

