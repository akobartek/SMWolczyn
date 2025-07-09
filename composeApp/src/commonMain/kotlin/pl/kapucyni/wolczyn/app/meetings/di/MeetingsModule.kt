package pl.kapucyni.wolczyn.app.meetings.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.meetings.data.FirebaseMeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.usecases.DrawGroupsUseCase
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.MeetingsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsViewModel

val meetingsModule = module {
    factory<MeetingsRepository> { FirebaseMeetingsRepository(get()) }
    single { DrawGroupsUseCase() }

    viewModel { (meetingId: Int, user: User?, email: String?) ->
        SigningsViewModel(meetingId, user, email, get())
    }
    viewModel { MeetingsViewModel(get()) }
    viewModel { (meetingId: Int, userType: UserType) ->
        ParticipantsViewModel(meetingId, userType, get())
    }
    viewModel { (meetingId: Int, email: String) ->
        ParticipantDetailsViewModel(meetingId, email, get())
    }
    viewModel { (meetingId: Int) -> MeetingWorkshopsViewModel(meetingId, get()) }
    viewModel { (meetingId: Int) -> MeetingGroupsViewModel(meetingId, get(), get()) }
}