package pl.kapucyni.wolczyn.app.quiz.presentation.model

import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizAnswer
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizQuestion

sealed class QuizScreenAction {
    data class UpdateUserId(val userId: String) : QuizScreenAction()

    data object StartQuizClicked : QuizScreenAction()

    data class AnswerSelected(val question: QuizQuestion, val answer: QuizAnswer) :
        QuizScreenAction()

    data object FinishQuizClicked : QuizScreenAction()
}