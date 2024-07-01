package pl.kapucyni.wolczyn.app.schedule.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.schedule.data.repository.ScheduleRepositoryImpl
import pl.kapucyni.wolczyn.app.schedule.data.sources.BasicScheduleSource
import pl.kapucyni.wolczyn.app.schedule.data.sources.FirestoreScheduleSource
import pl.kapucyni.wolczyn.app.schedule.domain.repository.ScheduleRepository
import pl.kapucyni.wolczyn.app.schedule.domain.usecases.GetScheduleUseCase
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleViewModel

val scheduleModule = module {
    single { FirestoreScheduleSource() }
    single { BasicScheduleSource() }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get()) }
    single { GetScheduleUseCase(get()) }

    viewModel { ScheduleViewModel(get()) }
}