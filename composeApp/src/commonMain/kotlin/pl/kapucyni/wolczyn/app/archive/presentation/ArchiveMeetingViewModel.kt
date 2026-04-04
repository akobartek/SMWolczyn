package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveMeetingByNumberUseCase
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingScreenEvent.NavigateUp
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen

class ArchiveMeetingViewModel(
    savedStateHandle: SavedStateHandle,
    private val getArchiveMeetingByNumberUseCase: GetArchiveMeetingByNumberUseCase,
) : BasicViewModel<ArchiveMeeting?>() {

    private val args = savedStateHandle.toRoute<Screen.ArchiveMeeting>()

    private val _events = Channel<ArchiveMeetingScreenEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getArchiveMeetingByNumberUseCase(args.meetingNumber).collect { meeting ->
                    if (meeting == null)
                        _events.send(NavigateUp)
                    else
                        _state.update { meeting }
                }
            }
        }
    }
}