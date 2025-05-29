package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanFailure
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanSuccess
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateSearchQuery
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateTypesFilter
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateWorkshopsFilter

@OptIn(FlowPreview::class)
class ParticipantsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Participant>>() {

    private var allParticipants = listOf<Participant>()

    private val _filterState = MutableStateFlow(ParticipantsFilterState())
    val filterState = _filterState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                meetingsRepository.getMeetingParticipants(meetingId)
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { participants ->
                        allParticipants = participants
                        _screenState.update { State.Success(filterParticipants(filterState.value)) }
                    }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                meetingsRepository.getAllWorkshops().map { it.name }.let { workshops ->
                    if (workshops.isEmpty()) return@launch

                    _filterState.update { state ->
                        state.copy(
                            workshops = workshops + "",
                            selectedWorkshops = workshops + "",
                        )
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            _filterState
                .onEach { _screenState.update { State.Loading } }
                .debounce { currentState ->
                    if (
                        currentState.query.isNotBlank()
                        || currentState.selectedTypes.size != currentState.participantTypes.size
                        || currentState.selectedWorkshops.size != currentState.workshops.size
                    ) 1226L // PDK
                    else 0L
                }
                .collect { currentState ->
                    if (allParticipants.isNotEmpty())
                        _screenState.update { State.Success(filterParticipants(currentState)) }
                }
        }
    }

    fun handleAction(action: ParticipantsScreenAction) {
        when (action) {
            is UpdateSearchQuery -> updateSearchQuery(action.query)
            is UpdateTypesFilter -> updateTypesFilter(action.elementSelected)
            is UpdateWorkshopsFilter -> updateWorkshopsFilter(action.elementSelected)
            is QrScanSuccess -> handleQrScanSuccess(action.email)
            is QrScanFailure -> handleQrScanFailure()
        }
    }

    private fun filterParticipants(filterState: ParticipantsFilterState) =
        if (
            filterState.query.isBlank()
            && filterState.selectedTypes.size == filterState.participantTypes.size
            && filterState.selectedWorkshops.size == filterState.workshops.size
        )
            allParticipants
        else
            allParticipants.filter {
                val searchResult = it.firstName.contains(filterState.query, ignoreCase = true)
                        || it.lastName.contains(filterState.query, ignoreCase = true)
                        || it.city.contains(filterState.query, ignoreCase = true)
                        || it.email.contains(filterState.query, ignoreCase = true)
                        || it.pesel.contains(filterState.query, ignoreCase = true)

                val typeResult = filterState.selectedTypes.contains(it.type)
                val workshopsResult = filterState.selectedWorkshops.contains(it.workshop)

                searchResult && typeResult && workshopsResult
            }

    private fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(query = query) }
    }

    private fun updateWorkshopsFilter(elementSelected: String) {
        _filterState.update { state ->
            state.copy(
                selectedWorkshops =
                    if (state.selectedWorkshops.contains(elementSelected)) {
                        state.selectedWorkshops - elementSelected
                    } else {
                        state.selectedWorkshops + elementSelected
                    },
            )
        }
    }

    private fun updateTypesFilter(elementSelected: ParticipantType) {
        _filterState.update { state ->
            state.copy(
                selectedTypes =
                    if (state.selectedTypes.contains(elementSelected)) {
                        state.selectedTypes - elementSelected
                    } else {
                        state.selectedTypes + elementSelected
                    },
            )
        }
    }

    private fun handleQrScanSuccess(email: String) {
        viewModelScope.launch {
            allParticipants.firstOrNull { it.email == email }?.let { participant ->
                SnackbarController.sendEvent(SnackbarEvent.QrCodeScanningSuccess)
            } ?: SnackbarController.sendEvent(SnackbarEvent.QrCodeUserNotFound)
        }
    }

    private fun handleQrScanFailure() {
        viewModelScope.launch {
            SnackbarController.sendEvent(SnackbarEvent.QrCodeScanningFailed)
        }
    }
}