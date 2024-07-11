package pl.kapucyni.wolczyn.app.workshops.prayer.domain.usecases

import pl.kapucyni.wolczyn.app.workshops.prayer.domain.repository.PrayerWorkshopRepository

class GetPrayerTasksUseCase(private val prayerWorkshopRepository: PrayerWorkshopRepository) {
    operator fun invoke() = prayerWorkshopRepository.getTasks()
}