package pl.kapucyni.wolczyn.app.auth.presentation.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.ResetPasswordMessageSent
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SignInError
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SignedIn
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.VerifyEmailMessageSent
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail

class SignInViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val email = savedStateHandle.toRoute<Screen.SignIn>().email

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
                SnackbarController.sendEvent(VerifyEmailMessageSent)
            }
            authRepository.signOut()
        }
    }

    fun signIn() {
        val email = _state.value.email.trim()
        val password = _state.value.password.trim()
        if (validateInput(email, password).not()) return

        toggleLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signIn(email, password)
            toggleLoading(false)
            _state.update {
                if (result.isSuccess && result.getOrDefault(false)) {
                    SnackbarController.sendEvent(SignedIn)
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
                            SnackbarController.sendEvent(SignInError)
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
                    SnackbarController.sendEvent(ResetPasswordMessageSent)
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

    private fun validateInput(email: String, password: String): Boolean {
        val emailError = if (email.isValidEmail()) null else EmailErrorType.INVALID
        val passwordError = if (password.isNotBlank()) null else PasswordErrorType.EMPTY
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                password = password,
                passwordError = passwordError,
            )
        }
        return emailError == null && passwordError == null
    }
}