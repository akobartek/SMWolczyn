package pl.kapucyni.wolczyn.app.admin.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminData
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun AdminDataListItem(
    data: AdminData,
    onPromotionActivation: (String, Boolean) -> Unit,
    onPromotionDelete: ((String) -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WolczynText(
            text = data.name,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Checkbox(
            checked = data.isChecked,
            onCheckedChange = { checked -> onPromotionActivation(data.id, checked) },
        )
        onPromotionDelete?.let {
            IconButton(onClick = { onPromotionDelete(data.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}