package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_navigate_up
import smwolczyn.composeapp.generated.resources.wolczyn_logo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductPhotosPager(
    photos: List<String>,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { photos.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(photos) {
        pagerState.scrollToPage(0)
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            key = { photos[it] }
        ) { index ->
            AsyncImage(
                model = photos[index],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Res.drawable.wolczyn_logo),
                modifier = Modifier.aspectRatio(3 / 4f)
            )
        }
        IconButton(onClick = onBackPressed, modifier = Modifier.padding(top = 20.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                tint = wolczynColors.primary,
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