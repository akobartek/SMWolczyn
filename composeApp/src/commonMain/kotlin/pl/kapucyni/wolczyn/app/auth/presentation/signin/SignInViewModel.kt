package pl.kapucyni.wolczyn.app.auth.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.EmailNotVerifiedException
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.EmailErrorType
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.NoInternetAction
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail

class SignInViewModel(
    email: String,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInScreenState(email = email))
    val state: StateFlow<SignInScreenState> = _state.asStateFlow()

    fun updateEmail(value: String) {
        _state.update { it.copy(email = value, emailError = null) }
    }

    fun updatePassword(value: String) {
        _state.update { it.copy(password = value, passwordError = null) }
    }

    fun hideNoInternetDialog() {
        _state.update { it.copy(noInternetAction = null) }
    }

    fun toggleForgottenPasswordDialogVisibility() {
        _state.update {
            it.copy(
                forgottenPasswordDialogVisible = !it.forgottenPasswordDialogVisible,
                forgottenPasswordDialogError = false,
            )
        }
    }

    fun toggleEmailUnverifiedDialogVisibility(resend: Boolean) {
        _state.update { it.copy(emailUnverifiedDialogVisible = !it.emailUnverifiedDialogVisible) }
        viewModelScope.launch {
            if (resend) {
                authRepository.sendVerificationEmail()
                SnackbarController.sendEvent(SnackbarEvent.VerifyEmailMessageSent)
            }
            authRepository.signOut()
        }
    }

    fun signIn() {
        if (validateInput().not()) return
        toggleLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signIn(state.value.email, state.value.password)
            toggleLoading(false)
            _state.update {
                if (result.isSuccess && result.getOrDefault(false)) {
                    SnackbarController.sendEvent(SnackbarEvent.SignedIn)
                    it.copy(isSignedIn = true)
                } else result.exceptionOrNull()?.let { exc ->
                    when (exc) {
                        is FirebaseAuthInvalidUserException ->
                            it.copy(emailError = EmailErrorType.NO_USER)

                        is EmailNotVerifiedException ->
                            it.copy(emailUnverifiedDialogVisible = true)

                        is FirebaseNetworkException ->
                            it.copy(noInternetAction = NoInternetAction.SIGN_IN)

                        is FirebaseAuthInvalidCredentialsException ->
                            it.copy(passwordError = PasswordErrorType.INVALID)

                        is FirebaseAuthException ->
                            it.copy(passwordError = PasswordErrorType.UNKNOWN)

                        else -> {
                            SnackbarController.sendEvent(SnackbarEvent.SignInError)
                            it
                        }
                    }
                } ?: it
            }
        }
    }

    fun sendResetPasswordEmail(email: String) {
        _state.update { it.copy(forgottenPasswordDialogError = !email.isValidEmail()) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.sendRecoveryEmail(email)
            _state.update {
                if (result.isSuccess && result.getOrDefault(false)) {
                    SnackbarController.sendEvent(SnackbarEvent.ResetPasswordMessageSent)
                    it.copy(forgottenPasswordDialogVisible = false)
                } else result.exceptionOrNull()?.let { exc ->
                    when (exc) {
                        is FirebaseNetworkException -> it.copy(
                            noInternetAction = NoInternetAction.RESET_PASSWORD,
                            forgottenPasswordDialogVisible = false
                        )

                        else -> it.copy(forgottenPasswordDialogError = true)
                    }
                } ?: it.copy(forgottenPasswordDialogError = true)
            }
        }
    }

    private fun toggleLoading(value: Boolean) = _state.update { it.copy(loading = value) }

    private fun validateInput(): Boolean {
        val newState = _state.value.let { state ->
            state.copy(
                emailError = if (state.email.isValidEmail()) null else EmailErrorType.INVALID,
                passwordError = when {
                    state.password.isBlank() -> PasswordErrorType.EMPTY
                    else -> null
                }
            )
        }
        _state.update { newState }
        return newState.emailError == null && newState.passwordError == null
    }
}