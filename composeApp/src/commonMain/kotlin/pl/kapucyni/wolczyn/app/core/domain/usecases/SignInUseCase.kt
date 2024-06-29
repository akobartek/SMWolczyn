package pl.kapucyni.wolczyn.app.core.domain.usecases

import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class SignInUseCase(private val coreRepository: CoreRepository) {
    suspend operator fun invoke(login: String, password: String) =
        coreRepository.signIn(login, password)
}