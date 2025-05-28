package pl.kapucyni.wolczyn.app.meetings.presentation.participants.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FollowTheSigns
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.SearchTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynBottomSheet
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.ParticipantsFilterState
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.ParticipantsScreenAction
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.ParticipantsScreenAction.UpdateSearchQuery
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.ParticipantsScreenAction.UpdateTypesFilter
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.ParticipantsScreenAction.UpdateWorkshopsFilter
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.filter_empty
import smwolczyn.composeapp.generated.resources.participant_type_title
import smwolczyn.composeapp.generated.resources.workshops

@Composable
fun ParticipantsFilteringBottomSheet(
    state: ParticipantsFilterState,
    handleAction: (ParticipantsScreenAction) -> Unit,
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    WolczynBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SearchTextField(
                value = state.query,
                onValueChange = { handleAction(UpdateSearchQuery(it)) },
            )

            FilterList(
                title = Res.string.participant_type_title,
                imageVector = Icons.AutoMirrored.Filled.FollowTheSigns,
                allElements = state.participantTypes.map {
                    it to stringResource(it.stringRes)
                },
                selectedElements = state.selectedTypes,
                onElementSelected = { handleAction(UpdateTypesFilter(it)) },
            )

            if (state.workshops.isNotEmpty()) {
                FilterList(
                    title = Res.string.workshops,
                    imageVector = Icons.Default.Construction,
                    allElements = state.workshops.map {
                        it to (it.takeIf { it.isNotBlank() }
                            ?: stringResource(Res.string.filter_empty))
                    },
                    selectedElements = state.selectedWorkshops,
                    onElementSelected = { handleAction(UpdateWorkshopsFilter(it)) },
                )
            }

            HeightSpacer(24.dp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> FilterList(
    title: StringResource,
    imageVector: ImageVector,
    allElements: List<Pair<T, String>>,
    selectedElements: List<T>,
    onElementSelected: (T) -> Unit,
) {
    HeightSpacer(16.dp)
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    HeightSpacer(16.dp)
    Row(verticalAlignment = Alignment.CenterVertically) {
        WidthSpacer(12.dp)
        Icon(
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
        )
        WidthSpacer(12.dp)
        WolczynText(
            text = stringResource(title),
            textStyle = MaterialTheme.typography.titleMedium,
        )
    }
    HeightSpacer(8.dp)
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        allElements.forEach { (element, text) ->
            val selected = selectedElements.contains(element)
            FilterChip(
                label = {
                    WolczynText(
                        text = text,
                        textStyle = MaterialTheme.typography.bodySmall,
                    )
                },
                selected = selected,
                onClick = { onElementSelected(element) },
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}