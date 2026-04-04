package pl.kapucyni.wolczyn.app.common.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BasicViewModel<T> : ViewModel() {

    protected val _state = MutableStateFlow<T?>(null)
    val state = _state.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    protected fun setLoading(value: Boolean) = _loading.update { value }
}