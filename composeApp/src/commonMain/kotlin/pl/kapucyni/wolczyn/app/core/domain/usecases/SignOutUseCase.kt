package pl.kapucyni.wolczyn.app.core.domain.usecases

import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class SignOutUseCase(private val coreRepository: CoreRepository) {
    suspend operator fun invoke() = coreRepository.signOut()
}