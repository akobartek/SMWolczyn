package pl.kapucyni.wolczyn.app.quiz.domain.usecases

import pl.kapucyni.wolczyn.app.quiz.domain.repository.QuizRepository

class CheckIfUserIsAllowedUseCase(private val quizRepository: QuizRepository) {
    suspend operator fun invoke(userId: String) =
        quizRepository.checkIfUserIsAllowed(userId)
}