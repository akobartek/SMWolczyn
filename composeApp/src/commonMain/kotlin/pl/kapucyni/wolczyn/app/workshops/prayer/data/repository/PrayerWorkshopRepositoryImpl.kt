package pl.kapucyni.wolczyn.app.workshops.prayer.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.workshops.prayer.data.sources.FirestorePrayerWorkshopSource
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.repository.PrayerWorkshopRepository

class PrayerWorkshopRepositoryImpl(
    private val firestoreSource: FirestorePrayerWorkshopSource,
) : PrayerWorkshopRepository {
    override fun getTasks(): Flow<List<PrayerWorkshopTask>> =
        firestoreSource.getWorkshopTasks()
            .map { firestoreTasks ->
                firestoreTasks
                    .sortedBy { it.order }
                    .filter { it.isAvailable }
                    .map { it.toDomainObject() }
            }
}