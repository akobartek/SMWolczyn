package pl.kapucyni.wolczyn.app.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BasicViewModel<T>: ViewModel() {

    sealed class State<out X> {
        data object Loading : State<Nothing>()
        data class Success<X>(val data: X) : State<X>()
    }

    protected val _screenState = MutableStateFlow<State<T>>(State.Loading)
    val screenState: StateFlow<State<T>> = _screenState.asStateFlow()
}