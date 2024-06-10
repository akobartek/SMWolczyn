package pl.kapucyni.wolczyn.app.songbook.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.songbook.domain.model.Song
import pl.kapucyni.wolczyn.app.songbook.domain.usecases.FilterSongsUseCase

@OptIn(FlowPreview::class)
class SongBookViewModel(private val filterSongsUseCase: FilterSongsUseCase) :
    BasicViewModel<List<Song>>() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery
                .onEach { _screenState.update { State.Loading } }
                .debounce { query ->
                    if (query.isNotBlank()) 1226L // PDK
                    else 0L
                }
                .collect { query ->
                    _screenState.update { State.Success(filterSongsUseCase(query)) }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }
}