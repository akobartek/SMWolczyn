package pl.kapucyni.wolczyn.app.schedule.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.schedule.data.repository.ScheduleRepositoryImpl
import pl.kapucyni.wolczyn.app.schedule.domain.repository.ScheduleRepository
import pl.kapucyni.wolczyn.app.schedule.domain.usecase.GetScheduleUseCase
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleViewModel

val scheduleModule = module {
    single<ScheduleRepository> { ScheduleRepositoryImpl() }
    single { GetScheduleUseCase(get()) }

    single { ScheduleViewModel(get()) }
}