package pl.kapucyni.wolczyn.app.auth.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pl.kapucyni.wolczyn.app.auth.domain.model.User

interface AuthRepository {
    val currentUser: StateFlow<User?>
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserIdIfExists(email: String): String
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(user: User, password: String): Result<Boolean>
    suspend fun getEmailFromVerificationCode(code: String): Result<Unit>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun sendRecoveryEmail(email: String): Result<Boolean>
    suspend fun getEmailFromResetCode(code: String): Result<String>
    suspend fun confirmPasswordReset(code: String, newPassword: String): Result<Unit>
    suspend fun sendVerificationEmail()
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit?>
    suspend fun repairMissingProfileIfNeeded(): Result<Boolean>
}