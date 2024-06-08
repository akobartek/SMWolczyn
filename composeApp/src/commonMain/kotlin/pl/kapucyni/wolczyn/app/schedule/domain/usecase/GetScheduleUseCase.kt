package pl.kapucyni.wolczyn.app.schedule.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay
import pl.kapucyni.wolczyn.app.schedule.domain.repository.ScheduleRepository

class GetScheduleUseCase(private val scheduleRepository: ScheduleRepository) {
    operator fun invoke(): Flow<List<ScheduleDay>> = scheduleRepository.getSchedule()
}