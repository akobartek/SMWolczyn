package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel

class AdminViewModel(
    private val getAppDataUseCase: GetAppDataUseCase<FirestoreData>
): BasicViewModel<FirestoreData>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAppDataUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { data -> _screenState.update { State.Success(data) } }
            } catch (_: Exception) {
            }
        }
    }
}