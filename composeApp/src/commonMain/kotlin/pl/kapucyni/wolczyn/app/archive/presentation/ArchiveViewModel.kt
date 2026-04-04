package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveUseCase
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel

class ArchiveViewModel(
    private val getArchiveUseCase: GetArchiveUseCase,
) : BasicViewModel<List<ArchiveMeeting>>() {

    init {
        viewModelScope.launch {
            runCatching {
                getArchiveUseCase().collect { archive ->
                    _state.update { archive }
                }
            }
        }
    }
}