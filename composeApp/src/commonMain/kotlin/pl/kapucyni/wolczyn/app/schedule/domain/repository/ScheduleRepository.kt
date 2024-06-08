package pl.kapucyni.wolczyn.app.schedule.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay

interface ScheduleRepository {
    fun getSchedule(): Flow<List<ScheduleDay>>
}