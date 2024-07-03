package pl.kapucyni.wolczyn.app.breviary.domain.usecases

import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository

class ClearBreviaryDbUseCase(private val breviaryRepository: BreviaryRepository) {
    suspend operator fun invoke(date: String) = breviaryRepository.clearBreviaryDb(date)
}