package pl.kapucyni.wolczyn.app.breviary.data.repository

import androidx.compose.ui.graphics.Color
import pl.kapucyni.wolczyn.app.breviary.data.sources.WebBreviarySource
import pl.kapucyni.wolczyn.app.breviary.domain.model.Breviary
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType
import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository

class BreviaryRepositoryImpl(
    private val webBreviarySource: WebBreviarySource
) : BreviaryRepository {

    override suspend fun checkIfThereAreMultipleOffices(date: String): Result<Map<String, String>?> =
        webBreviarySource.checkIfThereAreMultipleOffices(date)

    override suspend fun loadBreviary(
        office: String,
        date: String,
        type: BreviaryType,
        onlyHtml: Boolean,
        accentColor: Color
    ): Result<Breviary> =
        webBreviarySource.loadBreviary(office, date, type, onlyHtml, accentColor)
}