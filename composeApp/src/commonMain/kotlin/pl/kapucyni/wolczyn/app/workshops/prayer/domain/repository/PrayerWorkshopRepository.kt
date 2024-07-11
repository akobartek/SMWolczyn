package pl.kapucyni.wolczyn.app.workshops.prayer.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask

interface PrayerWorkshopRepository {
    fun getTasks(): Flow<List<PrayerWorkshopTask>>
}