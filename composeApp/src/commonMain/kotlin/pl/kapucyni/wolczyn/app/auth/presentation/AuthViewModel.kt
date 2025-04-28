package pl.kapucyni.wolczyn.app.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.*
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private var userJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getUserIdentifier().collect { userId ->
                userId?.let { startObservingUser(it) }
                    ?: clearUser()
            }
        }
    }

    fun handleAction(action: AuthAction) {
        when (action) {
            is SignOut -> signOut()
            is ResetPassword -> resetPassword()
            is DeleteAccount -> deleteAccount()
        }
    }

    private fun clearUser() {
        userJob?.cancel()
        _user.value = null
    }

    private fun startObservingUser(userId: String) {
        userJob = viewModelScope.launch {
            try {
                authRepository.getCurrentUser(userId = userId)
                    .stateIn(this, SharingStarted.WhileSubscribed(5000L), null)
                    .collect { user ->
                        _user.value = user ?: User(id = userId)
                    }
            } catch (e: FirebaseFirestoreException) {
                userJob?.cancel()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            SnackbarController.sendEvent(SnackbarEvent.SignedOut)
        }
    }

    private fun resetPassword() {
        user.value?.email?.let {
            viewModelScope.launch { authRepository.sendRecoveryEmail(it) }
        }
    }

    private fun deleteAccount() {
        userJob?.cancel()
        viewModelScope.launch {
            authRepository.deleteAccount()
            SnackbarController.sendEvent(SnackbarEvent.AccountDeleted)
        }
    }
}