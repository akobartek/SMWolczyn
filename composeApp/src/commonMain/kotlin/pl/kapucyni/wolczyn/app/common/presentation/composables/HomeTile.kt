package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeTile(
    nameRes: StringResource,
    nameAlignment: Alignment,
    height: Dp,
    backgroundColor: Color,
    image: @Composable BoxScope.() -> Unit,
    additionalContent: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors().copy(containerColor = backgroundColor),
        modifier = modifier.height(height)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(nameAlignment)
            ) {
                WolczynTitleText(
                    text = stringResource(nameRes),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(20.dp)
                )
                additionalContent()
            }

            image()
        }

    }
}