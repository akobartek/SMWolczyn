package pl.kapucyni.wolczyn.app.quiz.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class QuizState {
    WAITING,
    ONGOING,
    FINISHED,
    NOT_AVAILABLE,
    ;
}