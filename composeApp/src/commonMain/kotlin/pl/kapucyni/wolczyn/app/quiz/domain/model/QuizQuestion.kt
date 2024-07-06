package pl.kapucyni.wolczyn.app.quiz.domain.model

data class QuizQuestion(
    val question: String,
    val answers: List<QuizAnswer>,
)
