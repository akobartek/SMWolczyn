package pl.kapucyni.wolczyn.app.theme

import androidx.compose.ui.graphics.Color

data class WolczynMainColors(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val alert: Color,
) {
    companion object {
        val Light = WolczynMainColors(
            primary = Color(0xFF96551D),
            secondary = Color(0xFFDFB896),
            accent = Color(0xFFD69D6B),
            alert = Color(0xFFFD9B44),
        )
        val Dark = WolczynMainColors(
            primary = Color(0xFFE2A269),
            secondary = Color(0xFF694220),
            accent = Color(0xFF945B29),
            alert = Color(0xFFBB5802),
        )
    }
}
