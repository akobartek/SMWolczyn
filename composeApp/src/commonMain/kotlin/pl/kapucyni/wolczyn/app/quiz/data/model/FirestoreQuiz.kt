package pl.kapucyni.wolczyn.app.quiz.data.model

import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.quiz.domain.model.Quiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizAnswer
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizQuestion
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState

@Serializable
data class FirestoreQuiz(
    val questions: Map<String, List<String>>,
    val state: QuizState,
) {
    fun toDomainObject() =
        Quiz(
            state = state,
            questions = questions.map { (fsQuestion, fsAnswers) ->
                QuizQuestion(
                    question = fsQuestion,
                    answers = fsAnswers.map { fsAnswer ->
                        QuizAnswer(
                            isCorrect = fsAnswer.startsWith(CORRECT_ANSWER_PREFIX),
                            answer = fsAnswer.removePrefix(CORRECT_ANSWER_PREFIX),
                        )
                    },
                )
            },
        )

    companion object {
        private const val CORRECT_ANSWER_PREFIX = "* "
    }
}
