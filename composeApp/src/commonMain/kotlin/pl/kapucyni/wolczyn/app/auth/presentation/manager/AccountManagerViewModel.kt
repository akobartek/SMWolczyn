package pl.kapucyni.wolczyn.app.auth.presentation.manager

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.utils.normalizeMultiplatform

class AccountManagerViewModel(
    private val authRepository: AuthRepository,
) : BasicViewModel<List<User>>() {

    private var allUsers = listOf<User>()
    private var query = ""

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                authRepository.getAllUsers().collect { users ->
                    allUsers = users
                    filterUsers()
                }
            }
        }
    }

    fun updateUserType(user: User, changedUser: User) {
        if (
            user.userType == changedUser.userType
            && user.permits == changedUser.permits
        ) return

        viewModelScope.launch(Dispatchers.Default) {
            authRepository.updateUser(changedUser)
        }
    }

    fun searchUsers(query: String) {
        this.query = query.trim()
        viewModelScope.launch {
            filterUsers()
        }
    }

    private suspend fun filterUsers() =
        if (query.isBlank())
            _state.update { allUsers }
        else
            allUsers.filter {
                it.searchableUserString()
                    .normalizeMultiplatform()
                    .contains(query.normalizeMultiplatform(), ignoreCase = true)
            }.let { users ->
                if (users.isNotEmpty())
                    _state.update { users }
                else {
                    val validTypes = UserType.entries.filter { type ->
                        getString(type.stringRes).contains(query, ignoreCase = true)
                    }
                    if (validTypes.isEmpty()) _state.update { emptyList() }
                    else allUsers.filter { it.userType in validTypes }.let { typeFilteredUsers ->
                        _state.update { typeFilteredUsers }
                    }
                }
            }

    private fun User.searchableUserString() =
        "$firstName $lastName $email $city"
}