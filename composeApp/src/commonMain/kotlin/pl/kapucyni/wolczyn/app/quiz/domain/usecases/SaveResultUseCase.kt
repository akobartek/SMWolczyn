package pl.kapucyni.wolczyn.app.quiz.domain.usecases

import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult
import pl.kapucyni.wolczyn.app.quiz.domain.repository.QuizRepository

class SaveResultUseCase(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(result: QuizResult) =
        quizRepository.saveResults(result)
}