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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.*
import pl.kapucyni.wolczyn.app.auth.presentation.resetpassword.ResetPasswordDialogState
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.AccountDeleteFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.AccountDeleted
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.ResetPasswordSuccess
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SignedOut
import pl.kapucyni.wolczyn.app.common.utils.validatePassword
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

    private val _resetPasswordDialogState = MutableStateFlow<ResetPasswordDialogState?>(null)
    val resetPasswordDialogState = _resetPasswordDialogState.asStateFlow()

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

        viewModelScope.launch(Dispatchers.Default) {
            DeepLinkManager.resetCode.collect { resetCode ->
                resetCode?.let {
                    authRepository.getEmailFromResetCode(resetCode)
                        .onSuccess { email ->
                            _resetPasswordDialogState.update {
                                ResetPasswordDialogState(
                                    resetCode = resetCode,
                                    email = email,
                                )
                            }
                        }
                        .onFailure { println(it.message) }
                } ?: run { closeResetDialog() }
            }
        }
    }

    fun handleAction(action: AuthAction) {
        when (action) {
            is SignOut -> signOut()
            is ResetPassword -> resetPassword()
            is DeleteAccount -> deleteAccount()
            is CloseResetDialog -> closeResetDialog()
            is SetNewPassword -> setNewPassword(action.password)
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
            } catch (_: FirebaseFirestoreException) {
                userJob?.cancel()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            SnackbarController.sendEvent(SignedOut)
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
                .onSuccess { SnackbarController.sendEvent(AccountDeleted) }
                .onFailure {
                    SnackbarController.sendEvent(AccountDeleteFailed)
                    user.value?.let { authRepository.updateUser(it) }
                }
        }
    }

    private fun closeResetDialog() {
        _resetPasswordDialogState.update { null }
    }

    private fun setNewPassword(password: String) {
        val resetPasswordState = _resetPasswordDialogState.value ?: return
        _resetPasswordDialogState.update { resetPasswordState.copy(loading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            password.validatePassword()?.let { error ->
                _resetPasswordDialogState.update {
                    resetPasswordState.copy(loading = false, passwordError = error)
                }
            } ?: run {
                authRepository.confirmPasswordReset(
                    code = resetPasswordState.resetCode,
                    newPassword = password,
                ).onSuccess {
                    DeepLinkManager.clearCode()
                    SnackbarController.sendEvent(ResetPasswordSuccess)
                }.onFailure {
                    _resetPasswordDialogState.update {
                        resetPasswordState.copy(loading = false, resetFailed = true)
                    }
                }
            }
        }
    }
}