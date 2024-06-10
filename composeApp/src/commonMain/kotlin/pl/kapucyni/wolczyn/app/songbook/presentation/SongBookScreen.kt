package pl.kapucyni.wolczyn.app.songbook.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.songbook.domain.model.Song
import pl.kapucyni.wolczyn.app.songbook.presentation.composables.SongBookSearchBar
import pl.kapucyni.wolczyn.app.songbook.presentation.composables.SongCard
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.empty_search_list
import smwolczyn.composeapp.generated.resources.ic_empty_song_book
import kotlin.math.roundToInt

@Composable
fun SongBookScreen(
    onBackPressed: () -> Unit,
    viewModel: SongBookViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()
    val searchQuery by viewModel.searchQuery.collectAsStateMultiplatform()

    SongBookScreenContent(
        state = screenState,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongBookScreenContent(
    state: BasicViewModel.State<List<Song>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val searchBarHeightDp = 56.dp + 12.dp
    val searchBarHeightPx = with(LocalDensity.current) { searchBarHeightDp.roundToPx().toFloat() }
    var searchBarOffsetHeightPx by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = searchBarOffsetHeightPx + delta
                searchBarOffsetHeightPx = newOffset.coerceIn(-searchBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        stickyHeader {
            SongBookSearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChange,
                onBackPressed = onBackPressed,
                modifier = Modifier
                    .offset {
                        IntOffset(x = 0, y = searchBarOffsetHeightPx.roundToInt())
                    }
            )
        }

        when (state) {
            is BasicViewModel.State.Loading -> item { LoadingBox() }
            is BasicViewModel.State.Success -> {
                if (state.data.isNotEmpty()) {
                    items(items = state.data, key = { it.title }) { song ->
                        SongCard(
                            song = song,
                            modifier = Modifier.fillParentMaxWidth()
                        )
                    }
                } else item {
                    EmptyListInfo(
                        messageRes = Res.string.empty_search_list,
                        drawableRes = Res.drawable.ic_empty_song_book
                    )
                }
            }
        }
    }
}