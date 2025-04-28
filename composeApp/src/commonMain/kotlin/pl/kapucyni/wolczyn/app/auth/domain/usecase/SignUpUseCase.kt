package pl.kapucyni.wolczyn.app.auth.domain.usecase

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        city: String,
        birthday: Long?,
    ) = authRepository.signUp(
        user = User(
            email = email,
            firstName = firstName,
            lastName = lastName,
            city = city,
            birthday = birthday?.let { Timestamp.fromMilliseconds(it.toDouble()) }
                ?: Timestamp.now(),
        ),
        password = password,
    )
}