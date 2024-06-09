package pl.kapucyni.wolczyn.app.kitchen.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuItem

@Composable
fun KitchenMenuItem(item: KitchenMenuItem) {
    Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)) {
        WolczynText(
            text = item.name.uppercase(),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-1).sp
            )
        )
        if (item.variants.isNotBlank())
            WolczynText(
                text = item.variants,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-1).sp
                )
            )
    }
}