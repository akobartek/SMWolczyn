package pl.kapucyni.wolczyn.app.meetings.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.meetings.data.FirebaseMeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.MeetingsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsViewModel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsViewModel

val meetingsModule = module {
    factory<MeetingsRepository> { FirebaseMeetingsRepository(get()) }

    viewModel { (meetingId: Int, user: User?) -> SigningsViewModel(meetingId, user, get()) }
    viewModel { MeetingsViewModel(get()) }
    viewModel { (meetingId: Int) -> ParticipantsViewModel(meetingId, get()) }
}