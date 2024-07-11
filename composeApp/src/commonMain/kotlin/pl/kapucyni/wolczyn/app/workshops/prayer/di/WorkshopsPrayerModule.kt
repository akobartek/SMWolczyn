package pl.kapucyni.wolczyn.app.workshops.prayer.di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.workshops.prayer.data.repository.PrayerWorkshopRepositoryImpl
import pl.kapucyni.wolczyn.app.workshops.prayer.data.sources.FirestorePrayerWorkshopSource
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.repository.PrayerWorkshopRepository
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.usecases.GetPrayerTasksUseCase
import pl.kapucyni.wolczyn.app.workshops.prayer.presentation.PrayerWorkshopViewModel

val workshopsPrayerModule = module {
    single { FirestorePrayerWorkshopSource() }
    single<PrayerWorkshopRepository> { PrayerWorkshopRepositoryImpl(get()) }
    single { GetPrayerTasksUseCase(get()) }

    viewModelOf(::PrayerWorkshopViewModel)
}