package pl.kapucyni.wolczyn.app.quiz.presentation.model

import pl.kapucyni.wolczyn.app.quiz.domain.model.Quiz

data class QuizScreenState(
    val isLoading: Boolean = true,
    val quiz: Quiz? = null,
    val loadingFailed: Boolean = false,
    val userNotAllowed: Boolean = false,
    val userId: String = "",
    val quizStarted: Boolean = false,
    val quizFinished: Boolean = false,
    val score: Int = 0,
)