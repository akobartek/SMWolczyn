package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenLayout(
    title: String,
    onBackPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    actionIcon: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(title = title, onBackPressed = onBackPressed, actionIcon = actionIcon)
        content()
        Spacer(modifier = Modifier.height(12.dp))
    }
}