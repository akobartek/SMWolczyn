package pl.kapucyni.wolczyn.app.auth.data

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreDocument
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.EmailNotVerifiedException
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.common.utils.deleteObject
import pl.kapucyni.wolczyn.app.common.utils.saveObject

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository {

    override fun getUserIdentifier() = auth.authStateChanged.map { it?.uid }

    override fun getCurrentUser(userId: String): Flow<User?> =
        firestore.getFirestoreDocument<User?>(
            collectionName = COLLECTION_USERS,
            documentId = userId,
        ).map { user ->
            user ?: auth.currentUser?.let { User(id = it.uid, email = it.email.orEmpty()) }
        }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password)
            authResult.user?.let { user ->
                if (user.isEmailVerified)
                    Result.success(true)
                else
                    Result.failure(EmailNotVerifiedException())
            } ?: Result.failure(Exception())
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }

    override suspend fun signUp(user: User, password: String): Result<Boolean> {
        return try {
            withContext(NonCancellable) {
                val authResult = auth.createUserWithEmailAndPassword(user.email, password)
                authResult.user?.let { authUser ->
                    authUser.sendEmailVerification()
                    firestore.saveObject(
                        collectionName = COLLECTION_USERS,
                        id = authUser.uid,
                        data = user.copy(id = authUser.uid),
                    )
                    signOut()
                    Result.success(true)
                } ?: Result.failure(Exception())
            }
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> = kotlin.runCatching {
        firestore.saveObject(
            collectionName = COLLECTION_USERS,
            id = user.id,
            data = user,
        )
    }

    override suspend fun sendRecoveryEmail(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email)
            Result.success(true)
        } catch (exc: Exception) {
            Result.failure(exc)
        }
    }

    override suspend fun sendVerificationEmail() {
        try {
            auth.currentUser?.sendEmailVerification()
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() = kotlin.runCatching {
        withContext(NonCancellable) {
            auth.currentUser?.let { user ->
                firestore.deleteObject(
                    collectionName = COLLECTION_USERS,
                    id = user.uid,
                )
                user.delete()
            }
        }
    }

    private companion object {
        const val COLLECTION_USERS = "users"
    }
}