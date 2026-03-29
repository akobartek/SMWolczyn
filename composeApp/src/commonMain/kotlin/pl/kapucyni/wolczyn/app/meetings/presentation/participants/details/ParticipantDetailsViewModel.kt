package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmSuccess
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsScreenEvent.NavigateUp

class ParticipantDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<Participant>() {

    private val args = savedStateHandle.toRoute<Screen.ParticipantDetails>()

    private val _events = Channel<ParticipantDetailsScreenEvent>()
    val events = _events.receiveAsFlow()

    private val _group = MutableStateFlow<Group?>(null)
    val group = _group.asStateFlow()

    init {
        setSuccessState()
    }

    fun confirmUserSigning() {
        val participant = (screenState.value as? State.Success)?.data ?: return
        val currentUser = authRepository.currentUser.value ?: return
        viewModelScope.launch(Dispatchers.Default) {
            setLoadingState()
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
                _screenState.update { State.Success(participant) }
                SnackbarController.sendEvent(SigningConfirmFailed)
            }
        }
    }

    private fun setSuccessState() {
        viewModelScope.launch(Dispatchers.Default) {
            coroutineScope {
                val participantAsync = async {
                    meetingsRepository.getParticipant(args.meetingId, args.email)
                }
                val groupAsync = async {
                    meetingsRepository.getParticipantGroup(args.meetingId, args.email)
                }

                participantAsync.await()?.let { participant ->
                    _group.update { groupAsync.await() }
                    _screenState.update { State.Success(participant) }
                } ?: onLoadingFailure()
            }
        }
    }

    private fun setLoadingState() {
        _screenState.update { State.Loading }
    }

    private suspend fun onLoadingFailure() {
        SnackbarController.sendEvent(SigningConfirmFailed)
        _events.send(NavigateUp)
    }
}