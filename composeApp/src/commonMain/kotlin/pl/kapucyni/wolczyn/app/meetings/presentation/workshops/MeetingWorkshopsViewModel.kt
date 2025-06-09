package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

class MeetingWorkshopsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Pair<Workshop, Int>>>() {

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

    fun updateWorkshop(workshop: Workshop, available: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.updateWorkshop(workshop.copy(available = available))
        }
    }
}