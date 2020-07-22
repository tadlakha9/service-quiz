package com.soprabanking.dxp.quiz.resource

import com.soprabanking.dxp.dok.PayloadDokRegistry.quizDok
import com.soprabanking.dxp.dok.PayloadDokRegistry.typeOfDuration
import com.soprabanking.dxp.quiz.model.api.answer
import com.soprabanking.dxp.quiz.model.api.question
import com.soprabanking.dxp.quiz.model.api.quiz

fun apiDok() {
    typeOfDuration = String::class
    answer {
        label = "A possible answer to a question"
        valid = "True if the answer is the valid one, defaults to false"
    }
    question {
        label = "Title of the question"
        duration = "Maximum time a user has to answer, defaults to 30 seconds"
        answers = "Array containing proposed answers"
    }
    quiz {
        id = "Quiz unique identifier"
        title = "Unique quiz title"
        duration = "Maximum duration, sum of all questions duration"
        questions = "Array containing questions"
    }
    quizResource {
        tag = "quiz"
        findAll {
            description = "Find all"
            summary = "Find all available quizzes. This endpoint supports pagination. For this sample security has not been enabled."
            response { body = quizDok }
        }
        create {
            description = "Create"
            summary = "Creates a new Quiz. For this sample security has not been enabled."
            request { body = quizDok }
            response { body = quizDok }
        }
    }
}
