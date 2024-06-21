package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveMeetingByNumberUseCase
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel

class ArchiveMeetingViewModel(
    private val getArchiveMeetingByNumberUseCase: GetArchiveMeetingByNumberUseCase,
) : BasicViewModel<ArchiveMeeting?>() {

    fun init(number: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getArchiveMeetingByNumberUseCase(number)
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { meeting -> _screenState.update { State.Success(meeting) } }
            } catch (_: Exception) {
            }
        }
    }
}