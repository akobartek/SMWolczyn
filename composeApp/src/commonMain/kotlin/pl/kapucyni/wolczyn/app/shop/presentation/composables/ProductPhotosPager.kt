package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import pl.kapucyni.wolczyn.app.theme.appColorSecondary
import pl.kapucyni.wolczyn.app.theme.appColorTertiary
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_navigate_up

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductPhotosPager(
    photos: List<String>,
    onBackPressed: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { photos.size })
    val scope = rememberCoroutineScope()
    val colors = listOf(appColorPrimary, appColorSecondary, appColorTertiary)

    Box {
        // TODO() -> replace with image
        HorizontalPager(
            state = pagerState,
            key = { photos[it] }
        ) { index ->
            Spacer(
                modifier = Modifier
                    .background(colors[index % 3])
                    .aspectRatio(3 / 4f)
            )
        }
        IconButton(onClick = onBackPressed, modifier = Modifier.padding(top = 20.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                tint = appColorPrimary,
                contentDescription = stringResource(Res.string.cd_navigate_up),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
                    .padding(6.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            tint = Color.Black,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                }

        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            tint = Color.Black,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
        )
    }
}