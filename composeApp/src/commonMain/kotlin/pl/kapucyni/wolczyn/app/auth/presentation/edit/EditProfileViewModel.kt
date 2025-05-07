package pl.kapucyni.wolczyn.app.auth.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.usecase.UpdateUserUseCase
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.SaveData
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.ToggleNoInternetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateCity
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateLastName
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent

class EditProfileViewModel(
    user: User,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {

    private var currentUser: User = user
    private val _state = MutableStateFlow(
        EditProfileScreenState(
            firstName = user.firstName,
            lastName = user.lastName,
            city = user.city,
            birthdayDate = user.birthday?.let { it.seconds * 1000 },
        )
    )
    val state: StateFlow<EditProfileScreenState> = _state.asStateFlow()

    fun handleAction(action: EditProfileAction) {
        when (action) {
            is UpdateFirstName -> updateFirstName(action.firstName)
            is UpdateLastName -> updateLastName(action.lastName)
            is UpdateCity -> updateCity(action.city)
            is UpdateBirthday -> updateBirthdayDate(action.millis)
            is SaveData -> saveData()
            is ToggleNoInternetDialog -> hideNoInternetDialog()
        }
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

    private fun hideNoInternetDialog() {
        _state.update { it.copy(noInternetDialogVisible = false) }
    }

    private fun saveData() {
        if (validateInput().not()) return
        toggleLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                updateUserUseCase(
                    user = currentUser,
                    firstName = firstName,
                    lastName = lastName,
                    city = city,
                    birthday = birthdayDate,
                    userType = currentUser.userType,
                )
            }.apply {
                toggleLoading(false)
                onSuccess {
                    SnackbarController.sendEvent(SnackbarEvent.EditProfileSuccess)
                    with(state.value) {
                        currentUser = currentUser.copy(
                            firstName = firstName,
                            lastName = lastName,
                            city = city,
                            birthday = birthdayDate?.let { Timestamp.fromMilliseconds(it.toDouble()) },
                        )
                    }
                }
                onFailure { throwable ->
                    when (throwable) {
                        is FirebaseNetworkException ->
                            _state.update { it.copy(noInternetDialogVisible = true) }

                        else -> {
                            SnackbarController.sendEvent(SnackbarEvent.DataSaveError)
                        }
                    }
                }
            }
        }
    }

    private fun toggleLoading(value: Boolean) = _state.update { it.copy(loading = value) }

    private fun validateInput(): Boolean {
        val newState = with(state.value) {
            copy(
                firstName = firstName.trim(),
                firstNameError = firstName.trim().isBlank(),
                lastName = lastName.trim(),
                lastNameError = lastName.trim().isBlank(),
                city = city.trim(),
                cityError = city.trim().isBlank(),
                birthdayError =
                    birthdayDate?.let { it > Clock.System.now().toEpochMilliseconds() } ?: false,
            )
        }
        _state.update { newState }
        val userChanged = currentUser.firstName != newState.firstName
                || currentUser.lastName != newState.lastName
                || currentUser.city != newState.city
                || currentUser.birthday?.toMilliseconds()?.toLong() != newState.birthdayDate
        return newState.firstNameError.not()
                && newState.lastNameError.not()
                && newState.cityError.not()
                && newState.birthdayError.not()
                && userChanged
    }
}