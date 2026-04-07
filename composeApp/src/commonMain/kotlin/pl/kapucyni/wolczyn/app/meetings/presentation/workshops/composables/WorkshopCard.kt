package pl.kapucyni.wolczyn.app.meetings.presentation.workshops.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Gender
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import pl.kapucyni.wolczyn.app.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopCard(
    workshop: Workshop,
    count: Int,
    onAvailableChange: (Boolean) -> Unit,
    onGenderChange: (Gender) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
        ) {
            WolczynText(
                text = "${workshop.name} ($count)",
                textStyle = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
            Switch(
                checked = workshop.available,
                onCheckedChange = onAvailableChange,
            )
        }
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
        ) {
            Gender.entries.forEachIndexed { index, gender ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = Gender.entries.size,
                    ),
                    onClick = { onGenderChange(gender) },
                    selected = workshop.gender == gender,
                    label = {
                        Icon(
                            imageVector = vectorResource(gender.iconRes),
                            contentDescription = null,
                        )
                    },
                    icon = {},
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WorkshopCardPreview() {
    AppTheme {
        WorkshopCard(
            workshop = Workshop(name = "Testowe", available = true),
            count = 67,
            onAvailableChange = {},
            onGenderChange = {},
        )
    }
}