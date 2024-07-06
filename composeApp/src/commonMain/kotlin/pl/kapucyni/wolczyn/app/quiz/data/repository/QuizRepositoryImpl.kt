package pl.kapucyni.wolczyn.app.quiz.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizSource
import pl.kapucyni.wolczyn.app.quiz.domain.model.Quiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult
import pl.kapucyni.wolczyn.app.quiz.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val firestoreQuizSource: FirestoreQuizSource,
): QuizRepository {
    override fun getQuiz(): Flow<Quiz?> =
        firestoreQuizSource.getQuiz().map { it?.toDomainObject() } // TODO Shuffle questions and answers

    override suspend fun saveResults(result: QuizResult) =
        firestoreQuizSource.saveResult(result)

    override suspend fun checkIfUserIsAllowed(userId: String): Boolean =
        firestoreQuizSource.checkIfUserStartedQuizBefore(userId)
}