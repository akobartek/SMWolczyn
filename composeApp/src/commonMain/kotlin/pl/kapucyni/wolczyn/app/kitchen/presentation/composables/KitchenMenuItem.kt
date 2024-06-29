package pl.kapucyni.wolczyn.app.kitchen.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuItem

@Composable
fun KitchenMenuItem(item: KitchenMenuItem) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 4.dp, bottom = if (item.variants.isNotBlank()) 0.dp else 4.dp)
    ) {
        WolczynText(
            text = item.name.uppercase(),
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-1).sp,
            )
        )
        if (item.variants.isNotBlank())
            WolczynText(
                text = item.variants,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-1).sp,
                ),
                modifier = Modifier.offset(y = (-5).dp)
            )
    }
}