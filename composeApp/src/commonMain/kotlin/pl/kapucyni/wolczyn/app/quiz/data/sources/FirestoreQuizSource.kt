package pl.kapucyni.wolczyn.app.quiz.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.utils.checkIfDocumentExists
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreFirstAvailableDocument
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult

class FirestoreQuizSource(
    private val quizType: FirestoreQuizType,
) {
    fun getQuiz(): Flow<FirestoreQuiz?> =
        Firebase.firestore.getFirestoreFirstAvailableDocument(quizType.quizCollection)

    fun getResults(): Flow<List<QuizResult>> =
        Firebase.firestore.getFirestoreCollectionFlow(quizType.resultsCollection)

    suspend fun saveQuiz(quiz: FirestoreQuiz) =
        Firebase.firestore.saveObject(
            collectionName = quizType.quizCollection,
            id = quizType.quizCollection,
            data = quiz,
        )

    suspend fun saveResult(result: QuizResult) =
        Firebase.firestore.saveObject(
            collectionName = quizType.resultsCollection,
            id = result.userId,
            data = result,
        )

    suspend fun checkIfUserStartedQuizBefore(userId: String) =
        Firebase.firestore.checkIfDocumentExists(
            collectionName = quizType.resultsCollection,
            documentId = userId,
        )
}