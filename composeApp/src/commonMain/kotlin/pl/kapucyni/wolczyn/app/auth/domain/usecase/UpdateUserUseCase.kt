package pl.kapucyni.wolczyn.app.auth.domain.usecase

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType

class UpdateUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        user: User,
        firstName: String,
        lastName: String,
        city: String,
        birthday: Long?,
        userType: UserType,
    ) = authRepository.updateUser(
        user = user.copy(
            firstName = firstName,
            lastName = lastName,
            city = city,
            birthday = birthday?.let { Timestamp.fromMilliseconds(it.toDouble()) },
            userType = userType,
        ),
    )
}