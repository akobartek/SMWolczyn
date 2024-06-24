package pl.kapucyni.wolczyn.app.breviary.domain.repository

import androidx.compose.ui.graphics.Color
import pl.kapucyni.wolczyn.app.breviary.domain.model.Breviary
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType

interface BreviaryRepository {
    suspend fun checkIfThereAreMultipleOffices(date: String): Result<Map<String, String>?>
    suspend fun loadBreviary(
        office: String,
        date: String,
        type: BreviaryType,
        onlyHtml: Boolean = false,
        accentColor: Color,
    ): Result<Breviary>
}