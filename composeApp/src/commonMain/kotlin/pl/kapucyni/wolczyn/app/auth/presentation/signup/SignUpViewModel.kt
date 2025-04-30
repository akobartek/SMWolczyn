package pl.kapucyni.wolczyn.app.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.usecase.SignUpUseCase
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.*
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail

class SignUpViewModel(
    email: String,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpScreenState(email = email))
    val state: StateFlow<SignUpScreenState> = _state.asStateFlow()

    fun handleAction(action: SignUpAction) {
        when (action) {
            is UpdateEmail -> updateEmail(action.email)
            is UpdatePassword -> updatePassword(action.password)
            is TogglePasswordHidden -> updatePasswordHidden()
            is UpdateFirstName -> updateFirstName(action.firstName)
            is UpdateLastName -> updateLastName(action.lastName)
            is UpdateCity -> updateCity(action.city)
            is UpdateBirthday -> updateBirthdayDate(action.millis)
            is UpdateConsents -> updateConsents(action.value)
            is SignUp -> signUp()
            is ToggleSignUpSuccessDialog -> toggleSignUpSuccessVisibility()
            is ToggleAccountExistsDialog -> toggleAccountExistsVisibility()
            is ToggleNoInternetDialog -> hideNoInternetDialog()
        }
    }

    private fun updateEmail(value: String) {
        _state.update { it.copy(email = value, emailError = false) }
    }

    private fun updatePassword(value: String) {
        _state.update { it.copy(password = value, passwordError = null) }
    }

    private fun updatePasswordHidden() {
        _state.update { it.copy(passwordHidden = !it.passwordHidden) }
    }

    private fun updateFirstName(firstName: String) {
        _state.update { it.copy(firstName = firstName, firstNameError = false) }
    }

    private fun updateLastName(lastName: String) {
        _state.update { it.copy(lastName = lastName, lastNameError = false) }
    }

    private fun updateCity(city: String) {
        _state.update { it.copy(city = city, cityError = false) }
    }

    private fun updateBirthdayDate(value: Long) {
        _state.update { it.copy(birthdayDate = value, birthdayError = false) }
    }

    private fun updateConsents(value: Boolean) {
        _state.update { it.copy(consentsChecked = value) }
    }

    private fun toggleSignUpSuccessVisibility() {
        _state.update { it.copy(isSignedUpDialogVisible = !it.isSignedUpDialogVisible) }
    }

    private fun toggleAccountExistsVisibility() {
        _state.update { it.copy(accountExistsDialogVisible = !it.accountExistsDialogVisible) }
    }

    private fun hideNoInternetDialog() {
        _state.update { it.copy(noInternetDialogVisible = false) }
    }

    private fun signUp() {
        if (validateInput().not() || state.value.consentsChecked.not()) return
        toggleLoading(true)
        viewModelScope.launch {
            val result = with(state.value) {
                signUpUseCase(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    birthday = birthdayDate,
                )
            }
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
        val newState = with(state.value) {
            copy(
                email = email.trim(),
                emailError = email.trim().isValidEmail().not(),
                passwordError = when {
                    password.length < 8 -> PasswordErrorType.TOO_SHORT
                    password.isValidPassword().not() -> PasswordErrorType.WRONG
                    else -> null
                },
                firstName = firstName.trim(),
                firstNameError = firstName.trim().isBlank(),
                lastName = lastName.trim(),
                lastNameError = lastName.trim().isBlank(),
                city = city.trim(),
                cityError = city.trim().isBlank(),
                birthdayError = birthdayDate == null,
            )
        }
        _state.update { newState }
        return newState.emailError.not()
                && newState.passwordError == null
                && newState.firstNameError.not()
                && newState.lastNameError.not()
                && newState.cityError.not()
                && newState.birthdayError.not()
    }

    private fun CharSequence.isValidPassword(): Boolean {
        val passwordRegex = Regex("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,20})")
        return this.matches(passwordRegex)
    }
}