package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.SaveWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateIsAdding
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateWorkshop

class MeetingWorkshopsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Pair<Workshop, Int>>>() {

    private val _isAdding = MutableStateFlow<Boolean>(false)
    val isAdding = _isAdding.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.getMeetingParticipants(meetingId, UserType.ADMIN)
                .combine(meetingsRepository.getWorkshopsFlow()) { participants, workshops ->
                    workshops.map {
                        it to participants.count { participant -> participant.workshop == it.name }
                    }
                }
                .shareIn(this, SharingStarted.Lazily, 1)
                .collect { data -> _screenState.update { State.Success(data) } }
        }
    }

    fun handleAction(action: MeetingWorkshopsScreenAction) {
        when (action) {
            is UpdateWorkshop -> updateWorkshop(action.workshop, action.available)
            is UpdateIsAdding -> updateIsAdding(action.isAdding)
            is SaveWorkshop -> saveWorkshop(action.workshopName)
        }
    }

    private fun updateWorkshop(workshop: Workshop, available: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.saveWorkshop(workshop.copy(available = available))
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
            meetingsRepository.saveWorkshop(newWorkshop)
            _isLoading.update { false }
        }
    }
}