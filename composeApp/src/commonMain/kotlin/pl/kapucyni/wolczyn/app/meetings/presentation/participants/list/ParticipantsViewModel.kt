package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.QrCodeScanningFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.QrCodeScanningSuccess
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.QrCodeUserNotFound
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.common.utils.normalizeMultiplatform
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanFailure
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanSuccess
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.ToggleAllUsers
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateSearchQuery
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateSorting
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateTypesFilter
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.UpdateWorkshopsFilter
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenEvent.NavigateUp
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenEvent.ScanUserFound
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsSorting.ALPHABETICALLY
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsSorting.BIRTHDAY_ASC
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsSorting.BIRTHDAY_DESC
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsSorting.SIGNING_DATE_ASC
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsSorting.SIGNING_DATE_DESC

@OptIn(FlowPreview::class)
class ParticipantsViewModel(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<ParticipantsState>() {

    private val args = savedStateHandle.toRoute<Screen.MeetingParticipants>()

    private var allParticipants = listOf<Participant>()

    private val _filterState = MutableStateFlow(ParticipantsFilterState())
    val filterState = _filterState.asStateFlow()

    private val _events = Channel<ParticipantsScreenEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            authRepository.currentUser.value?.takeIf { it.userType != UserType.MEMBER }
                ?.let { user ->
                    meetingsRepository.getMeetingParticipants(args.meetingId, user.userType)
                        .onEach { participants ->
                            allParticipants = participants
                            _state.update {
                                ParticipantsState(
                                    meetingId = args.meetingId,
                                    participants = filterParticipants(filterState.value),
                                    userType = user.userType,
                                )
                            }
                        }
                        .catch { _events.send(NavigateUp) }
                    .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
            } ?: run { _events.send(NavigateUp) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                meetingsRepository.getAllWorkshops().map { it.name }.let { workshops ->
                    if (workshops.isEmpty()) return@launch

                    _filterState.update { state ->
                        state.copy(workshops = workshops + "")
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            _filterState
                .debounce { currentState ->
                    if (
                        currentState.query.isNotBlank()
                        || currentState.selectedTypes.isNotEmpty()
                        || currentState.selectedWorkshops.isNotEmpty()
                    ) 666L
                    else 0L
                }
                .collect { currentState ->
                    if (allParticipants.isNotEmpty())
                        _state.update {
                            it?.copy(participants = filterParticipants(currentState))
                        }
                }
        }
    }

    fun handleAction(action: ParticipantsScreenAction) {
        when (action) {
            is UpdateSearchQuery -> updateSearchQuery(action.query)
            is UpdateSorting -> updateSorting(action.sorting)
            is ToggleAllUsers -> toggleAllUsers(action.checked)
            is UpdateTypesFilter -> updateTypesFilter(action.elementSelected)
            is UpdateWorkshopsFilter -> updateWorkshopsFilter(action.elementSelected)
            is QrScanSuccess -> handleQrScanSuccess(action.email)
            is QrScanFailure -> handleQrScanFailure()
        }
    }

    private fun filterParticipants(filterState: ParticipantsFilterState) =
        if (
            filterState.query.isBlank()
            && filterState.selectedTypes.isEmpty()
            && filterState.selectedWorkshops.isEmpty()
            && filterState.onlyConfirmedParticipants.not()
        )
            allParticipants.getSortedList(filterState.sorting)
        else
            allParticipants.filter {
                val query = filterState.query
                    .replace(" ", "")
                    .trim()
                    .normalizeMultiplatform()
                val searchResult =
                    (it.firstName + it.lastName)
                        .replace(" ", "")
                        .trim()
                        .normalizeMultiplatform()
                        .contains(query, ignoreCase = true)
                            || it.city.normalizeMultiplatform().contains(query, ignoreCase = true)
                            || it.email.contains(query, ignoreCase = true)
                            || it.pesel.contains(query, ignoreCase = true)
                            || it.birthday.getFormattedDate().contains(query, ignoreCase = true)

                val signedResult = if (filterState.onlyConfirmedParticipants) it.paid else true

                val typeResult =
                    filterState.selectedTypes.isEmpty()
                            || filterState.selectedTypes.contains(it.type)

                val workshopsResult =
                    filterState.selectedWorkshops.isEmpty()
                            || filterState.selectedWorkshops.contains(it.workshop)

                searchResult && signedResult && typeResult && workshopsResult
            }.getSortedList(filterState.sorting)

    private fun List<Participant>.getSortedList(sorting: ParticipantsSorting) =
        with(Locale.current) {
            when (sorting) {
                ALPHABETICALLY -> this@getSortedList.sortedWith(
                    compareBy(
                        {
                            it.firstName.toLowerCase(this)
                                .replace("br. ", "")
                                .replace("s. ", "")
                        },
                        { it.lastName.toLowerCase(this) },
                    )
                )

                BIRTHDAY_ASC -> this@getSortedList.sortedBy { it.birthday.seconds }

                BIRTHDAY_DESC -> this@getSortedList.sortedByDescending { it.birthday.seconds }

                SIGNING_DATE_ASC -> this@getSortedList.sortedBy { it.createdAt.seconds }

                SIGNING_DATE_DESC -> this@getSortedList.sortedByDescending { it.createdAt.seconds }
            }
        }

    private fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(query = query) }
    }

    private fun updateSorting(sorting: ParticipantsSorting) {
        if (sorting == filterState.value.sorting) return

        _filterState.update { it.copy(sorting = sorting) }
    }

    private fun toggleAllUsers(checked: Boolean) {
        _filterState.update { it.copy(onlyConfirmedParticipants = checked) }
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
                SnackbarController.sendEvent(QrCodeScanningSuccess)
                _events.send(ScanUserFound(participant))
            } ?: SnackbarController.sendEvent(QrCodeUserNotFound)
        }
    }

    private fun handleQrScanFailure() {
        viewModelScope.launch {
            SnackbarController.sendEvent(QrCodeScanningFailed)
        }
    }
}