package pl.kapucyni.wolczyn.app.meetings.presentation.participants

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

class ParticipantsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
): BasicViewModel<List<Participant>>() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                meetingsRepository.getMeetingParticipants(meetingId)
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { meetings -> _screenState.update { State.Success(meetings) } }
            }
        }
    }
}