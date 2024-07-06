package pl.kapucyni.wolczyn.app.quiz.domain.usecases

import pl.kapucyni.wolczyn.app.quiz.domain.repository.QuizRepository

class GetQuizUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke() = quizRepository.getQuiz()
}