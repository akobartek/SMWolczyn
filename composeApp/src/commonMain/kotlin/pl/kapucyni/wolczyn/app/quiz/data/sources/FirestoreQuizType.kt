package pl.kapucyni.wolczyn.app.quiz.data.sources

enum class FirestoreQuizType(
    val quizCollection: String,
    val resultsCollection: String,
) {
    KITCHEN("kitchen_quiz", "kitchen_quiz_results")
    ;
}