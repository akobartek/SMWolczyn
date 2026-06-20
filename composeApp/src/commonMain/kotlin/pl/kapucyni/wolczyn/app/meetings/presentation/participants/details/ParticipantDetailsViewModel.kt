package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.navigation.ParticipantParameterType
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SaveFailure
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SaveSuccess
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmSuccess
import pl.kapucyni.wolczyn.app.common.utils.genderByPesel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import pl.kapucyni.wolczyn.app.meetings.domain.usecases.GetWorkshopsUseCase
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsScreenEvent.NavigateUp
import kotlin.reflect.typeOf

class ParticipantDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val meetingsRepository: MeetingsRepository,
    private val getWorkshopsUseCase: GetWorkshopsUseCase,
) : BasicViewModel<ParticipantDetailsState>() {

    private val args = savedStateHandle.toRoute<Screen.ParticipantDetails>(
        typeMap = mapOf(typeOf<Participant>() to ParticipantParameterType),
    )

    private val _events = Channel<ParticipantDetailsScreenEvent>()
    val events = _events.receiveAsFlow()

    private val _group = MutableStateFlow<Group?>(null)
    val group = _group.asStateFlow()

    private val _workshops = MutableStateFlow<List<Pair<Workshop, Int>>>(listOf())
    val workshops = _workshops.asStateFlow()

    init {
        initState()
    }

    fun confirmUserSigning() {
        val participant = args.participant
        val currentUser = authRepository.currentUser.value ?: return
        viewModelScope.launch(Dispatchers.Default) {
            setLoading(true)
            meetingsRepository.saveParticipant(
                meetingId = args.meetingId,
                participant = participant.copy(
                    consents = true,
                    underageConsents = true,
                    paid = true,
                    acceptedAt = Timestamp.now(),
                    acceptedBy = "${currentUser.firstName} ${currentUser.lastName}",
                    acceptedById = currentUser.id,
                ),
            ).onSuccess {
                SnackbarController.sendEvent(SigningConfirmSuccess)
                _events.send(NavigateUp)
            }.onFailure {
                setLoading(false)
                SnackbarController.sendEvent(SigningConfirmFailed)
            }
        }
    }

    fun changeUserWorkshop(newWorkshop: String) {
        val updatedParticipant = _state.value?.participant?.copy(workshop = newWorkshop) ?: return
        viewModelScope.launch(Dispatchers.Default) {
            setLoading(true)
            meetingsRepository.saveParticipant(
                meetingId = args.meetingId,
                participant = updatedParticipant,
            ).onSuccess {
                setLoading(false)
                SnackbarController.sendEvent(SaveSuccess)
                _state.update { it?.copy(participant = updatedParticipant) }
            }.onFailure {
                setLoading(false)
                SnackbarController.sendEvent(SaveFailure)
            }
        }
    }

    private fun initState() {
        val user = authRepository.currentUser.value ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val participant = args.participant
            val group = meetingsRepository.getParticipantGroup(args.meetingId, participant.email)
            val showData = user.isAdmin() || user.hasAccessToParticipantsData()
            val meetingsCount =
                if (showData.not()) null
                else meetingsRepository.getParticipantMeetingsCount(participant.pesel)
            _group.update { group }
            _state.update {
                ParticipantDetailsState(
                    participant = participant,
                    showData = showData,
                    allowWorkshopChange = user.canEditParticipantsWorkshop(),
                    meetingsCount = meetingsCount,
                )
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (args.participant.workshop.isNotBlank() && user.canEditParticipantsWorkshop()) {
                val gender = args.participant.pesel.genderByPesel()
                getWorkshopsUseCase(
                    meetingId = args.meetingId,
                    takeOnlyAvailable = true,
                ).collect { workshops ->
                    workshops.filter { it.first.allow(gender) }.let { filtered ->
                        _workshops.update { filtered }
                    }
                }
            }
        }
    }
}