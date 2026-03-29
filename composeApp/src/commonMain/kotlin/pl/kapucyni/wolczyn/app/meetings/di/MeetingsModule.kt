package pl.kapucyni.wolczyn.app.meetings.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.meetings.data.FirebaseMeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.usecases.DrawGroupsUseCase
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.MeetingsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsViewModel

val meetingsModule = module {
    factory<MeetingsRepository> { FirebaseMeetingsRepository(get()) }
    single { DrawGroupsUseCase() }

    viewModelOf(::SigningsViewModel)
    viewModelOf(::SigningsAdminViewModel)
    viewModelOf(::MeetingsViewModel)
    viewModelOf(::ParticipantsViewModel)
    viewModelOf(::ParticipantDetailsViewModel)
    viewModelOf(::MeetingWorkshopsViewModel)
    viewModelOf(::MeetingGroupsViewModel)
}