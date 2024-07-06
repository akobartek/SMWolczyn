package pl.kapucyni.wolczyn.app.quiz.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.quiz.domain.model.Quiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult

interface QuizRepository {
    fun getQuiz(): Flow<Quiz?>
    suspend fun saveResults(result: QuizResult)
    suspend fun checkIfUserIsAllowed(userId: String): Boolean
}