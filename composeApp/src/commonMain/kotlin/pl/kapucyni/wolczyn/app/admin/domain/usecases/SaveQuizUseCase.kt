package pl.kapucyni.wolczyn.app.admin.domain.usecases

import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz

class SaveQuizUseCase(private val adminRepository: AdminRepository<FirestoreData>) {
    suspend operator fun invoke(quiz: FirestoreQuiz) =
        adminRepository.saveQuiz(quiz)
}