package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.fab.FloatingButtonData
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_more_options

@Composable
fun WolczynFabMenu(
    visible: Boolean = true,
    items: List<FloatingButtonData>,
) {
    var fabListVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible.not())
            fabListVisible = false
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        val filteredItems = items.filter { it.enabled }
        filteredItems.forEachIndexed { index, buttonData ->
            val multiplier = filteredItems.size - index
            AnimatedVisibility(
                visible = fabListVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it * multiplier }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it * multiplier }),
            ) {
                WolczynFloatingActionButton(data = buttonData)
            }
        }
        AnimatedVisibility(visible) {
            AnimatedContent(targetState = fabListVisible) { visible ->
                if (visible) {
                    FloatingActionButton(
                        onClick = { fabListVisible = fabListVisible.not() },
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ) {
                        MainFabIcon(listVisible = visible)
                    }
                } else {
                    FloatingActionButton(onClick = { fabListVisible = fabListVisible.not() }) {
                        MainFabIcon(listVisible = visible)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainFabIcon(listVisible: Boolean) {
    Icon(
        imageVector =
            if (listVisible) Icons.Default.Close
            else Icons.Default.Settings,
        contentDescription = stringResource(Res.string.cd_more_options),
    )
}