package pl.kapucyni.wolczyn.app.core.domain.usecases

import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class GetAppConfigurationUseCase(private val coreRepository: CoreRepository) {
    operator fun invoke() = coreRepository.getAppConfiguration().map {
        it ?: AppConfiguration()
    }
}