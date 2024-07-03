package pl.kapucyni.wolczyn.app.breviary.domain.usecases

import pl.kapucyni.wolczyn.app.breviary.domain.model.BreviaryType
import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository

class CheckOfficesUseCase(private val breviaryRepository: BreviaryRepository) {
    suspend operator fun invoke(
        date: String,
        type: BreviaryType,
    ) = breviaryRepository.checkIfThereAreMultipleOffices(date, type)
}