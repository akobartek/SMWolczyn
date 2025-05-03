package pl.kapucyni.wolczyn.app.meetings.presentation.meetings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting

class MeetingsViewModel(
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Meeting>>() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                meetingsRepository.getAllMeetings()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { meetings -> _screenState.update { State.Success(meetings) } }
            }
        }
    }
}