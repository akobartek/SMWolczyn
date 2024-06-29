package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenLayout(
    title: String,
    onBackPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    actionIcon: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(it)
            }
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ScreenHeader(title = title, onBackPressed = onBackPressed, actionIcon = actionIcon)
            content()
            HeightSpacer(12.dp)
        }
    }
}