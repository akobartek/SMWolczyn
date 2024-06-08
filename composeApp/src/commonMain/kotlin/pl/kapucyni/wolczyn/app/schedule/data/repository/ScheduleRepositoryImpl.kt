package pl.kapucyni.wolczyn.app.schedule.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.kapucyni.wolczyn.app.schedule.data.sources.schedule
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay
import pl.kapucyni.wolczyn.app.schedule.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl: ScheduleRepository {
    override fun getSchedule(): Flow<List<ScheduleDay>> =
        flowOf(schedule)
}