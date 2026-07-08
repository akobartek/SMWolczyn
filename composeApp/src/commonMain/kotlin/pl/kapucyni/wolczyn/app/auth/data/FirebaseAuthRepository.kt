package pl.kapucyni.wolczyn.app.auth.data

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import dev.gitlive.firebase.auth.ActionCodeResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreDocument
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.EmailNotVerifiedException
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.utils.checkIfDocumentExists
import pl.kapucyni.wolczyn.app.common.utils.deleteObject
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionByFieldSync
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.core.domain.repository.LogRepository
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val logRepository: LogRepository,
    private val scope: CoroutineScope,
) : AuthRepository {


    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentUser: StateFlow<User?> = auth.authStateChanged.flatMapLatest { firebaseUser ->
        logRepository.setUserId(firebaseUser?.uid.orEmpty())
        firebaseUser?.let {
            firestore.getFirestoreDocument<User?>(
                collectionName = COLLECTION_USERS,
                documentId = firebaseUser.uid,
            ).map { user ->
                (user ?: User(id = firebaseUser.uid, email = firebaseUser.email.orEmpty())) as User?
            }.catch { exc ->
                logRepository.logException(message = "Błąd pobierania danych użytkownika", exc)
                emit(null)
            }
        } ?: flowOf(null)
    }.stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = null)

    override fun getAllUsers(): Flow<List<User>> =
        firestore.getFirestoreCollectionFlow<User>(collectionName = COLLECTION_USERS)
            .map { users ->
                val locale = Locale.current
                users.filter { user -> user.userType != UserType.ADMIN }
                    .sortedWith(
                        compareBy(
                            {
                                it.firstName.toLowerCase(locale)
                                    .replace("br. ", "")
                                    .replace("s. ", "")
                            },
                            { it.lastName.toLowerCase(locale) },
                        )
                    )
            }.catch { exc ->
                logRepository.logException(message = "Błąd pobierania listy użytkowników", exc)
                emit(listOf())
            }

    override suspend fun getUserIdIfExists(email: String): String = runCatching {
        firestore.getFirestoreCollectionByFieldSync<User>(
            collectionName = COLLECTION_USERS,
            fieldName = "email",
            fieldValue = email,
        )?.id.orEmpty()
    }.getOrDefault("")

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
                val authUser = authResult.user ?: throw Exception("Error while creating account")

                try {
                    firestore.saveObject(
                        collectionName = COLLECTION_USERS,
                        id = authUser.uid,
                        data = user.copy(id = authUser.uid),
                    )

                    authUser.sendEmailVerification()
                    signOut()
                    Result.success(true)

                } catch (exc: Exception) {
                    logRepository.log(message = "signup: Rollback wykonany dla ${user.email}")
                    authUser.delete()
                    signOut()
                    throw exc
                }
            }
        } catch (exc: Exception) {
            logRepository.logException(message = "Błąd podczas rejestracji z mailem ${user.email}", exc)
            Result.failure(exc)
        }
    }

    override suspend fun getEmailFromVerificationCode(code: String): Result<Unit> = runCatching {
        auth.applyActionCode(code)
    }.onFailure { exc ->
        if (exc is CancellationException) throw exc
    }

    override suspend fun updateUser(user: User): Result<Unit> = runCatching {
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

    override suspend fun getEmailFromResetCode(code: String): Result<String> = runCatching {
        try {
            auth.checkActionCode<ActionCodeResult.PasswordReset>(code).email
        } catch (_: UnsupportedOperationException) {
            ""
        }
    }

    override suspend fun confirmPasswordReset(code: String, newPassword: String) = runCatching {
        auth.confirmPasswordReset(code, newPassword)
    }

    override suspend fun sendVerificationEmail() {
        try {
            auth.currentUser?.sendEmailVerification()
        } catch (exc: Exception) {
            logRepository.logException(message = "Błąd podczas wysyłania maila weryfikacyjnego", exc)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() = runCatching {
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

    override suspend fun repairMissingProfileIfNeeded(): Result<Boolean> = runCatching {
        val authUser = auth.currentUser ?: return@runCatching false

        if (firestore.checkIfDocumentExists(COLLECTION_USERS, authUser.uid)) {
            return@runCatching true
        }

        val recoveredUser = User(
            id = authUser.uid,
            email = authUser.email.orEmpty(),
        )

        firestore.saveObject(
            collectionName = COLLECTION_USERS,
            id = authUser.uid,
            data = recoveredUser,
        )
        logRepository.log(message = "Naprawiono dane użytkownika o id: ${authUser.uid}")

        true
    }

    private companion object {
        const val COLLECTION_USERS = "users"
    }
}