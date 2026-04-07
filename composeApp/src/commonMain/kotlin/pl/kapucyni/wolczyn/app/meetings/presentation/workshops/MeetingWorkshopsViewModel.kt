package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Gender
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.SaveWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateIsAdding
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateAvailability
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateGender

class MeetingWorkshopsViewModel(
    savedStateHandle: SavedStateHandle,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Pair<Workshop, Int>>>() {

    private val meetingId = savedStateHandle.toRoute<Screen.MeetingWorkshops>().meetingId

    private val _isAdding = MutableStateFlow(false)
    val isAdding = _isAdding.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            meetingsRepository.getMeetingParticipants(meetingId)
                .combine(meetingsRepository.getWorkshopsFlow(meetingId)) { participants, workshops ->
                    workshops.map {
                        it to participants.count { participant -> participant.workshop == it.name }
                    }
                }
                .collect { data -> _state.update { data } }
        }
    }

    fun handleAction(action: MeetingWorkshopsScreenAction) {
        when (action) {
            is UpdateAvailability -> updateWorkshop(action.workshop, action.available)
            is UpdateGender -> updateGender(action.workshop, action.gender)
            is UpdateIsAdding -> updateIsAdding(action.isAdding)
            is SaveWorkshop -> saveWorkshop(action.workshopName)
        }
    }

    private fun updateWorkshop(workshop: Workshop, available: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.saveWorkshop(meetingId, workshop.copy(available = available))
        }
    }

    private fun updateGender(workshop: Workshop, gender: Gender) {
        if (workshop.gender == gender) return

        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.saveWorkshop(meetingId, workshop.copy(gender = gender))
        }
    }

    private fun updateIsAdding(isAdding: Boolean) {
        _isAdding.update { isAdding }
    }

    private fun saveWorkshop(workshopName: String) {
        if (workshopName.length <= 3) return

        updateIsAdding(false)
        _isLoading.update { true }
        val newWorkshop = Workshop(
            id = workshopName.lowercase().replace(" ", "_"),
            name = workshopName,
            available = false,
        )
        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.saveWorkshop(meetingId, newWorkshop)
            _isLoading.update { false }
        }
    }
}