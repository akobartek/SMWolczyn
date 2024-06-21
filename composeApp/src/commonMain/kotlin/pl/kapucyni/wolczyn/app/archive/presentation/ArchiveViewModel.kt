package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveUseCase
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel

class ArchiveViewModel(
    private val getArchiveUseCase: GetArchiveUseCase,
) : BasicViewModel<List<ArchiveMeeting>>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getArchiveUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { archive -> _screenState.update { State.Success(archive) } }
            } catch (_: Exception) {
            }
        }
    }
}