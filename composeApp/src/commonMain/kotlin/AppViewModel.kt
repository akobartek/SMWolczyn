import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.CloseResetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.CloseVerificationDialog
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.DeleteAccount
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.ResetPassword
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SetNewPassword
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SignOut
import pl.kapucyni.wolczyn.app.auth.presentation.emailverification.EmailVerificationDialogState
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

    private val _emailVerificationDialogState = MutableStateFlow<EmailVerificationDialogState?>(null)
    val emailVerificationDialogState = _emailVerificationDialogState.asStateFlow()

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
                        .onFailure {
                            println("CODE error $it\n")
                        }
                } ?: run { closeResetDialog() }
            }
        }

        viewModelScope.launch {
            DeepLinkManager.verificationCode.collect { verificationCode ->
                verificationCode?.let {
                    authRepository.getEmailFromVerificationCode(verificationCode)
                        .onSuccess {
                            logRepository.log(message = "Uruchomiono deeplink weryfikacji maila")
                            _emailVerificationDialogState.update {
                                EmailVerificationDialogState(
                                    verificationCode = verificationCode,
                                    success = true,
                                )
                            }
                            DeepLinkManager.clearVerificationCode()
                        }
                        .onFailure { exc ->
                            logRepository.logException(
                                message = "Uruchomiono zużyty deeplink weryfikacji maila",
                                exc = exc,
                            )
                            _emailVerificationDialogState.update {
                                EmailVerificationDialogState(
                                    verificationCode = verificationCode,
                                    success = false,
                                )
                            }
                            DeepLinkManager.clearVerificationCode()
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
            is CloseVerificationDialog -> closeVerificationDialog()
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

    private fun closeVerificationDialog() {
        _emailVerificationDialogState.update { null }
    }

    private fun closeResetDialog() {
        DeepLinkManager.clearResetCode()
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
                    DeepLinkManager.clearResetCode()
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
            val isProfileVerified = withTimeoutOrNull(2000) {
                userPreferencesRepository.isProfileVerified.first()
            } ?: true
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