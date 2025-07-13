package pl.kapucyni.wolczyn.app.auth.presentation.manager

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel

class AccountManagerViewModel(
    private val authRepository: AuthRepository,
) : BasicViewModel<List<User>>() {

    private var allUsers = listOf<User>()
    private var query = ""

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.getAllUsers()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { users ->
                        allUsers = users
                        filterUsers()
                    }
            }
        }
    }

    fun updateUserType(user: User, userType: UserType) {
        if (user.userType == userType) return

        viewModelScope.launch(Dispatchers.Default) {
            authRepository.updateUser(user.copy(userType = userType))
        }
    }

    fun searchUsers(query: String) {
        this.query = query.trim()
        filterUsers()
    }

    private fun filterUsers() =
        if (query.isBlank())
            _screenState.update { State.Success(allUsers) }
        else
            allUsers.filter {
                it.firstName.contains(query, ignoreCase = true)
                        || it.lastName.contains(query, ignoreCase = true)
                        || it.email.contains(query, ignoreCase = true)
            }.let { users -> _screenState.update { State.Success(users) } }
}