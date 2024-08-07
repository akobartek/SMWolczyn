package pl.kapucyni.wolczyn.app.breviary.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryDay
import pl.kapucyni.wolczyn.app.common.utils.BackHandler
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.breviary_list
import smwolczyn.composeapp.generated.resources.save_in_memory

@Composable
fun DownloadLayout(breviaryDay: BreviaryDay, onBackPressed: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp)
    ) {
        stringArrayResource(Res.array.breviary_list).forEachIndexed { index, name ->
            DownloadItem(name, breviaryDay.getValueByIndex(index))
        }
        DownloadItem(
            stringResource(Res.string.save_in_memory),
            if (breviaryDay.id == 0L) "" else breviaryDay.id.toString()
        )
    }

    BackHandler(onBack = onBackPressed)
}