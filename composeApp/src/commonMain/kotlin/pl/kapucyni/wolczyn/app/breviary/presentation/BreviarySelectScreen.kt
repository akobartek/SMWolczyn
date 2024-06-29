package pl.kapucyni.wolczyn.app.breviary.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.breviary_copyright
import smwolczyn.composeapp.generated.resources.breviary_list
import smwolczyn.composeapp.generated.resources.breviary_title

@Composable
fun BreviarySelectScreen(
    onBackPressed: () -> Unit,
    onSelected: (Int, String) -> Unit,
    viewModel: BreviarySelectViewModel = koinInject()
) {
    val daysFromToday by viewModel.daysFromToday.collectAsStateMultiplatform()
    val dateString = viewModel.getCorrectDaysString(daysFromToday)

    ScreenLayout(
        title = stringResource(Res.string.breviary_title) + dateString,
        onBackPressed = onBackPressed
    ) {
        BreviarySelectScreenContent(onSelected = { onSelected(it, viewModel.dateString) })
    }
}

@Composable
fun BreviarySelectScreenContent(
    onSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        stringArrayResource(Res.array.breviary_list).forEachIndexed { index, elem ->
            Column(modifier = Modifier.clickable { onSelected(index) }) {
                WolczynText(
                    text = elem,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = wolczynColors.primary,
                    ),
                    modifier = Modifier.padding(12.dp)
                )
                HorizontalDivider()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        WolczynText(
            text = stringResource(Res.string.breviary_copyright),
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Center,
                color = wolczynColors.primary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}