package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.core.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.home_title

@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 32.dp)
    ) {
        WolczynText(
            text = stringResource(Res.string.home_title).uppercase(),
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}