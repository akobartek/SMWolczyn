package pl.kapucyni.wolczyn.app.quiz.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDateTime

@Serializable
data class QuizResult(
    val userId: String,
    val score: Int = 0,
    val submittedAt: Long = Clock.System.now().toEpochMilliseconds(),
) {
    fun getFormattedSubmittedAt(): String =
        Instant.fromEpochMilliseconds(submittedAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .getFormattedDateTime()
}
