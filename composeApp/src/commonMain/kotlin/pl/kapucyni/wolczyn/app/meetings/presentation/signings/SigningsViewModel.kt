package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

class SigningsViewModel(
    private val meetingId: Int,
    private val user: User?,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<SigningsScreenState>() {

    init {
        viewModelScope.launch {
            coroutineScope {
                val workshops = async { meetingsRepository.getAvailableWorkshops() }
                val previousSigning = async {
                    user?.let { meetingsRepository.checkPreviousSigning(meetingId, it.email) }
                }

                previousSigning.await().let { participant ->
                    val state = State.Success(
                        data = SigningsScreenState(
                            isUserInfoEditable = user == null,
                            firstName = participant?.firstName ?: user?.firstName.orEmpty(),
                            lastName = participant?.lastName ?: user?.lastName.orEmpty(),
                            city = participant?.city ?: user?.city.orEmpty(),
                            email = participant?.email ?: user?.email.orEmpty(),
                            pesel = participant?.pesel.orEmpty(),
                            birthdayDate = (participant?.birthday ?: user?.birthday)
                                ?.toMilliseconds()?.toLong(),
                            type = participant?.type,
                            availableTypes = ParticipantType.entries.let {
                                if (user == null) it
                                else it - ParticipantType.ORGANISATION
                            },
                            selectedWorkshop = participant?.workshop,
                            availableWorkshops = workshops.await().map { it.name },
                        )
                    )
                    _screenState.update { state }
                }
            }
        }
    }

    fun handleAction(action: SigningsAction) {
        when (action) {
            is SigningsAction.UpdateSomething -> TODO()
            is SigningsAction.SaveData -> TODO()
            is SigningsAction.ToggleNoInternetDialog -> TODO()
        }
    }
}