package pl.kapucyni.wolczyn.app.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User

class AppViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private var userJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        startObservingUser()
    }

    private fun startObservingUser() {
        userJob = viewModelScope.launch(Dispatchers.IO) {
            authRepository.getCurrentUser()
                .stateIn(this, SharingStarted.WhileSubscribed(5000L), null)
                .collect { user -> _user.value = user }
        }
    }
}