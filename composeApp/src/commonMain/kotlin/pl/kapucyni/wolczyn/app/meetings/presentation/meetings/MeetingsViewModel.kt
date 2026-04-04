package pl.kapucyni.wolczyn.app.meetings.presentation.meetings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting

class MeetingsViewModel(
    coreRepository: CoreRepository,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<List<Meeting>>() {

    val openSigning = coreRepository.appConfiguration
        .map { it.openSigning }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = coreRepository.appConfiguration.value.openSigning,
        )

    init {
        viewModelScope.launch {
            runCatching {
                meetingsRepository.getAllMeetings().collect { meetings ->
                    _state.update { meetings }
                }
            }
        }
    }
}