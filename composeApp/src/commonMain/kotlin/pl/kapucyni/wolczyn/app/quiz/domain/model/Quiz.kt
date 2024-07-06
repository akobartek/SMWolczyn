package pl.kapucyni.wolczyn.app.quiz.domain.model

data class Quiz(
    val questions: List<QuizQuestion>,
    val state: QuizState,
)
