package pl.kapucyni.wolczyn.app.schedule.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.schedule.data.sources.BasicScheduleSource
import pl.kapucyni.wolczyn.app.schedule.data.sources.FirestoreScheduleSource
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay
import pl.kapucyni.wolczyn.app.schedule.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(
    private val firestoreSource: FirestoreScheduleSource,
    private val basicSource: BasicScheduleSource
) : ScheduleRepository {
    override fun getSchedule(): Flow<List<ScheduleDay>> =
        firestoreSource.getFirestoreSchedule().map { firestoreEvents ->
            val schedule = basicSource.getBasicSchedule()
            firestoreEvents.forEach { event ->
                var eventIndex = -1
                schedule.indexOfFirst { day ->
                    val index = day.events.indexOfFirst { it.id == event.id }
                    if (index > -1) eventIndex = index
                    eventIndex >= 0
                }
                    .takeIf { it >= 0 }
                    ?.let { dayIndex ->
                        schedule[dayIndex].events[eventIndex] =
                            event.toDomainObject(schedule[dayIndex].events[eventIndex])
                    }
            }
            schedule
        }
}