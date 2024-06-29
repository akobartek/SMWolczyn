package pl.kapucyni.wolczyn.app.core.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetGroupInfoUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetUserInfoUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.SignInUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.SignOutUseCase
import pl.kapucyni.wolczyn.app.core.presentation.model.AuthDialogState

class HomeScreenViewModel(
    private val getAppStateUseCase: GetAppStateUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getGroupInfoUseCase: GetGroupInfoUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
) : BasicViewModel<AppState>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAppStateUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { homeInfo -> _screenState.update { State.Success(homeInfo) } }
            } catch (_: Exception) {
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getUserInfoUseCase()?.let { user ->
                    _authState.update { it.signedIn(user) }
                }
            } catch (_: Exception) {
            }
        }
    }

    private val _authState = MutableStateFlow(AuthDialogState())
    val authState: StateFlow<AuthDialogState> = _authState.asStateFlow()

    fun showAuthDialog() {
        _authState.update { it.showDialog() }
    }

    fun hideAuthDialog() {
        _authState.update { it.hideDialog() }
    }

    fun checkDialog() {
        if (_authState.value.isDialogVisible) {
            _authState.update { it.hideDialog() }
            _authState.update { it.showDialog() }
        }
    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _authState.update { it.loading() }
                signInUseCase(
                    login = login,
                    password = password,
                )?.let { user ->
                    _authState.update { it.signedIn(user) }
                } ?: _authState.update { it.loginError() }
            } catch (_: Exception) {
                _authState.update { it.loginError() }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                signOutUseCase()
            } catch (_: Exception) {
            } finally {
                _authState.update { it.signedOut() }
            }
        }
    }

    fun loadGroupInfo() {
        if (_authState.value.group == null)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    getGroupInfoUseCase()?.let { group ->
                        _authState.update { it.groupLoaded(group) }
                    }
                } catch (_: Exception) {
                }
            }
    }
}