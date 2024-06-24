package pl.kapucyni.wolczyn.app.breviary.domain.usecases

import androidx.compose.ui.graphics.Color
import pl.kapucyni.wolczyn.app.breviary.domain.model.Breviary
import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType
import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository

class LoadBreviaryUseCase(private val breviaryRepository: BreviaryRepository) {
    suspend operator fun invoke(
        office: String,
        date: String,
        type: BreviaryType,
        accentColor: Color
    ): Result<Breviary> =
        breviaryRepository.loadBreviary(office, date, type, false, accentColor)
}