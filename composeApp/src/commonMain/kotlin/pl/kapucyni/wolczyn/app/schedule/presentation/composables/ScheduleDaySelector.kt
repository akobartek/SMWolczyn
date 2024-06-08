package pl.kapucyni.wolczyn.app.schedule.presentation.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import pl.kapucyni.wolczyn.app.theme.appColorTertiary

@Composable
fun RowScope.ScheduleDaySelector(
    day: Int,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(if (isSelected) appColorPrimary else appColorTertiary)
    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onPrimary else appColorPrimary
    )
    val height by animateDpAsState(if (isSelected) 104.dp else 90.dp)
    val weight by animateFloatAsState(if (isSelected) 1.2f else 1f)
    val numberTextSize by animateIntAsState(if (isSelected) 32 else 22)

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(53.dp),
        onClick = onClick,
        modifier = Modifier
            .weight(weight)
            .height(height)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            WolczynText(
                text = day.toString(),
                textStyle = TextStyle(
                    fontSize = numberTextSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                ),
            )
            WolczynText(
                text = name,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-1).sp,
                    color = contentColor,
                ),
            )
        }
    }
}