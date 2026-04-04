package pl.kapucyni.wolczyn.app.auth.presentation.edit

import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.usecase.UpdateUserUseCase
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.SaveData
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.ToggleNoInternetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateCity
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateLastName
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileScreenEvent.NavigateUp
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.DataSaveError
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.EditProfileSuccess
import kotlin.time.Clock

class EditProfileViewModel(
    authRepository: AuthRepository,
    private val updateUserUseCase: UpdateUserUseCase,
) : BasicViewModel<EditProfileScreenState>() {

    private var currentUser: User? = authRepository.currentUser.value

    private val _events = Channel<EditProfileScreenEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                currentUser = user
                user?.let {
                    _state.update { _ ->
                        EditProfileScreenState(
                            firstName = user.firstName,
                            lastName = user.lastName,
                            city = user.city,
                            birthdayDate = user.birthday?.let { it.seconds * 1000 },
                        )
                    }
                } ?: _events.send(NavigateUp)
            }
        }
    }

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
        _state.update { it?.copy(firstName = firstName, firstNameError = false) }
    }

    private fun updateLastName(lastName: String) {
        _state.update { it?.copy(lastName = lastName, lastNameError = false) }
    }

    private fun updateCity(city: String) {
        _state.update { it?.copy(city = city, cityError = false) }
    }

    private fun updateBirthdayDate(value: Long) {
        _state.update { it?.copy(birthdayDate = value, birthdayError = false) }
    }

    private fun hideNoInternetDialog() {
        _state.update { it?.copy(noInternetDialogVisible = false) }
    }

    private fun saveData() {
        val currentUser = currentUser ?: return
        val state = _state.value ?: return
        if (state.validateInput(currentUser).not()) return
        toggleLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            with(state) {
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
                    SnackbarController.sendEvent(EditProfileSuccess)
                }
                onFailure { throwable ->
                    when (throwable) {
                        is FirebaseNetworkException ->
                            _state.update { it?.copy(noInternetDialogVisible = true) }

                        else -> {
                            SnackbarController.sendEvent(DataSaveError)
                        }
                    }
                }
            }
        }
    }

    private fun toggleLoading(value: Boolean) = _state.update { it?.copy(loading = value) }

    private fun EditProfileScreenState.validateInput(currentUser: User): Boolean {
        val newState = copy(
            firstName = firstName.trim(),
            firstNameError = firstName.trim().isBlank(),
            lastName = lastName.trim(),
            lastNameError = lastName.trim().isBlank(),
            city = city.trim(),
            cityError = city.trim().isBlank(),
            birthdayError =
                birthdayDate?.let { it > Clock.System.now().toEpochMilliseconds() } ?: false,
        )
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