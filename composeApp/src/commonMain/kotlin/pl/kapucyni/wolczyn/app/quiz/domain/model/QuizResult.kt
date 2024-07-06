package pl.kapucyni.wolczyn.app.quiz.domain.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val userId: String,
    val score: Int = 0,
    val time: Long = Clock.System.now().toEpochMilliseconds(),
    val submittedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
