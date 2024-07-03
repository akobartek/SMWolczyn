package pl.kapucyni.wolczyn.app.breviary.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.breviary.domain.model.Breviary
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.CheckOfficesUseCase
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.LoadBreviaryUseCase

class BreviaryTextViewModel(
    private val checkOfficesUseCase: CheckOfficesUseCase,
    private val loadBreviaryUseCase: LoadBreviaryUseCase,
) : ViewModel() {

    sealed class State {
        data object Init : State()
        data object Loading : State()
        data class MultipleOffices(val offices: Map<String, String>) : State()
        data class BreviaryAvailable(val breviary: Breviary) : State()
        data class Failure(val processingFailed: Boolean, val downloadsClicked: Boolean = false) :
            State()

        data object Cancelled : State()
    }

    private var selectedOfficeLink = ""
    private lateinit var type: BreviaryType
    private var date = ""
    private var color = Color.White

    private val _screenState = MutableStateFlow<State>(State.Init)
    val screenState: StateFlow<State> = _screenState.asStateFlow()

    fun setup(position: Int, date: String, accentColor: Color) {
        this.type = BreviaryType.fromPosition(position)
        this.date = date
        this.color = accentColor
        if (screenState.value is State.Init)
            checkIfThereAreMultipleOffices()
    }

    fun checkIfThereAreMultipleOffices() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.update { State.Loading }
            val result = checkOfficesUseCase(date, type)
            if (result.isSuccess) {
                val offices = result.getOrNull()
                offices?.forEach { println(it.value) }
                if (offices == null) loadBreviary()
                else _screenState.update { State.MultipleOffices(offices) }
            } else {
//                val dbResult = dbLoadUseCase(date, type)
                _screenState.update {
//                    val value = dbResult.getOrNull()
//                    if (dbResult.isFailure || value == null || value.html.isBlank())
                    State.Failure(processingFailed = false)
//                    else State.BreviaryAvailable(dbResult.getOrNull()!!)
                }
            }
        }
    }

    fun officeSelected(office: String) {
        selectedOfficeLink = office
        loadBreviary(office)
    }

    private fun loadBreviary(office: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.update { State.Loading }
            val result = loadBreviaryUseCase(office, date, type, color)
            if (result.isSuccess && result.getOrNull() != null)
                _screenState.update { State.BreviaryAvailable(result.getOrNull()!!) }
            else {
//                val dbResult = dbLoadUseCase(date, type)
//                _screenState.update {
//                    val value = dbResult.getOrNull()
//                    if (dbResult.isFailure || value == null || value.html.isBlank())
                State.Failure(processingFailed = true)
//                    else State.BreviaryAvailable(dbResult.getOrNull()!!)
//                }
            }
        }
    }

    fun cancelScreen() {
        _screenState.update { State.Cancelled }
    }

    fun onDownloadsDialogClicked() {
        _screenState.update { State.Failure(processingFailed = true, downloadsClicked = true) }
    }
}