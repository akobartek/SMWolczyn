package pl.kapucyni.wolczyn.app.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import smwolczyn.composeapp.generated.resources.Poppins_Black
import smwolczyn.composeapp.generated.resources.Poppins_Bold
import smwolczyn.composeapp.generated.resources.Poppins_ExtraBold
import smwolczyn.composeapp.generated.resources.Poppins_Light
import smwolczyn.composeapp.generated.resources.Poppins_Medium
import smwolczyn.composeapp.generated.resources.Poppins_Regular
import smwolczyn.composeapp.generated.resources.Poppins_SemiBold
import smwolczyn.composeapp.generated.resources.Res


@Composable
internal fun poppinsFontFamily() = FontFamily(
    Font(Res.font.Poppins_Regular, FontWeight.Normal),
    Font(Res.font.Poppins_Black, FontWeight.Black),
    Font(Res.font.Poppins_ExtraBold, FontWeight.ExtraBold),
    Font(Res.font.Poppins_Bold, FontWeight.Bold),
    Font(Res.font.Poppins_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Poppins_Medium, FontWeight.Medium),
    Font(Res.font.Poppins_Light, FontWeight.Light),
)