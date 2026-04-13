import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.CloseResetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.DeleteAccount
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.ResetPassword
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SetNewPassword
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SignOut
import pl.kapucyni.wolczyn.app.auth.presentation.resetpassword.ResetPasswordDialogState
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.AccountDeleteFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.AccountDeleted
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.ResetPasswordSuccess
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SignedOut
import pl.kapucyni.wolczyn.app.common.utils.validatePassword
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.repository.LogRepository
import pl.kapucyni.wolczyn.app.core.presentation.UserPreferencesRepository

class AppViewModel(
    coreRepository: CoreRepository,
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val logRepository: LogRepository,
) : ViewModel() {

    val user: StateFlow<User?> = authRepository.currentUser

    val appConfiguration: StateFlow<AppConfiguration> = coreRepository.appConfiguration

    private val _resetPasswordDialogState = MutableStateFlow<ResetPasswordDialogState?>(null)
    val resetPasswordDialogState = _resetPasswordDialogState.asStateFlow()

    init {

        viewModelScope.launch {
            DeepLinkManager.resetCode.collect { resetCode ->
                resetCode?.let {
                    authRepository.getEmailFromResetCode(resetCode)
                        .onSuccess { email ->
                            logRepository.log(message = "Uruchomiono deeplink resetowania hasła")
                            _resetPasswordDialogState.update {
                                ResetPasswordDialogState(
                                    resetCode = resetCode,
                                    email = email,
                                )
                            }
                        }
                } ?: run { closeResetDialog() }
            }
        }

        checkAndRepairProfile()
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

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            userPreferencesRepository.setProfileVerified(false)
            SnackbarController.sendEvent(SignedOut)
        }
    }

    private fun resetPassword() {
        user.value?.email?.let {
            viewModelScope.launch { authRepository.sendRecoveryEmail(it) }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
                .onSuccess {
                    SnackbarController.sendEvent(AccountDeleted)
                    userPreferencesRepository.setProfileVerified(false)
                }
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

    private fun checkAndRepairProfile() {
        viewModelScope.launch {
            val isProfileVerified = userPreferencesRepository.isProfileVerified.first()
            logRepository.setCustomKey("is_profile_verified", isProfileVerified)
            if (isProfileVerified.not()) {
                val result = authRepository.repairMissingProfileIfNeeded()
                if (result.getOrDefault(false)) {
                    userPreferencesRepository.setProfileVerified(true)
                }
            }
        }
    }
}