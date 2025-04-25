package pl.kapucyni.wolczyn.app.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail

class SignUpViewModel(
    email: String,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpScreenState(email = email))
    val state: StateFlow<SignUpScreenState> = _state.asStateFlow()

    fun updateEmail(value: String) {
        _state.update { it.copy(email = value) }
    }

    fun updatePassword(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun updatePasswordHidden() {
        _state.update { it.copy(passwordHidden = !it.passwordHidden) }
    }

    fun updateBirthdayDate(value: Long) {
        _state.update { it.copy(birthdayDate = value) }
    }

    fun hideNoInternetDialog() {
        _state.update { it.copy(noInternetDialogVisible = false) }
    }

    fun toggleSignUpSuccessVisibility() {
        _state.update { it.copy(isSignedUpDialogVisible = !it.isSignedUpDialogVisible) }
    }

    fun toggleAccountExistsVisibility() {
        _state.update { it.copy(accountExistsDialogVisible = !it.accountExistsDialogVisible) }
    }

    fun signUp() {
        if (validateInput().not()) return
        toggleLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signUp(state.value.email, state.value.password)
            toggleLoading(false)
            _state.update {
                if (result.isSuccess && result.getOrDefault(false))
                    it.copy(isSignedUpDialogVisible = true)
                else result.exceptionOrNull()?.let { exc ->
                    when (exc) {
                        is FirebaseAuthUserCollisionException ->
                            it.copy(accountExistsDialogVisible = true)

                        is FirebaseNetworkException ->
                            it.copy(noInternetDialogVisible = true)

                        else -> {
                            SnackbarController.sendEvent(SnackbarEvent.SignUpError)
                            it
                        }
                    }
                } ?: it
            }
        }
    }

    private fun toggleLoading(value: Boolean) = _state.update { it.copy(loading = value) }

    private fun validateInput(): Boolean {
        val newState = _state.value.let { state ->
            state.copy(
                emailError = state.email.isValidEmail().not(),
                passwordError = when {
                    state.password.length < 8 -> PasswordErrorType.TOO_SHORT
                    !state.password.isValidPassword() -> PasswordErrorType.WRONG
                    else -> null
                }
            )
        }
        _state.update { newState }
        return newState.emailError.not() && newState.passwordError == null
    }

    private fun CharSequence.isValidPassword(): Boolean {
        val passwordRegex = Regex("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,20})")
        return this.matches(passwordRegex)
    }
}