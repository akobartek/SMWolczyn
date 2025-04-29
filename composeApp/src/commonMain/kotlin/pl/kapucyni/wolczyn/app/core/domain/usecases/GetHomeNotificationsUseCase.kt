package pl.kapucyni.wolczyn.app.core.domain.usecases

import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class GetHomeNotificationsUseCase(private val coreRepository: CoreRepository) {
    operator fun invoke() = coreRepository.getHomeNotifications()
}