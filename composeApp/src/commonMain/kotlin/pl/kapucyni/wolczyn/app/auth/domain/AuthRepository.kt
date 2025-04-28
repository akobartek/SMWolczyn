package pl.kapucyni.wolczyn.app.auth.domain

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.auth.domain.model.User

interface AuthRepository {
    fun getUserIdentifier(): Flow<String?>
    fun getCurrentUser(userId: String): Flow<User?>
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(user: User, password: String): Result<Boolean>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun sendRecoveryEmail(email: String): Result<Boolean>
    suspend fun sendVerificationEmail()
    suspend fun signOut()
    suspend fun deleteAccount()
}