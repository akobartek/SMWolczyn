package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun ScreenLayout(
    title: String?,
    onBackPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    actionIcon: (@Composable RowScope.() -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit) = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        floatingActionButton = floatingActionButton,
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus(true) },
        ) {
            title?.let {
                ScreenHeader(title = title, onBackPressed = onBackPressed, actionIcon = actionIcon)
            }
            content()
            HeightSpacer(12.dp)
        }
    }
}