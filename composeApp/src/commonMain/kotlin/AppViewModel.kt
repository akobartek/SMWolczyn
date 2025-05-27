import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.*
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppConfigurationUseCase

class AppViewModel(
    private val getAppConfigurationUseCase: GetAppConfigurationUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private var userJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _appConfiguration = MutableStateFlow<AppConfiguration?>(null)
    val appConfiguration: StateFlow<AppConfiguration?> = _appConfiguration.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            getAppConfigurationUseCase().collect {
                _appConfiguration.value = it
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            authRepository.getUserIdentifier().collect { userId ->
                userId?.takeIf { it.isNotBlank() }?.let { startObservingUser(it) }
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
                .onSuccess { SnackbarController.sendEvent(SnackbarEvent.AccountDeleted) }
                .onFailure {
                    SnackbarController.sendEvent(SnackbarEvent.AccountDeleteFailed)
                    user.value?.let { authRepository.updateUser(it) }
                }
        }
    }
}