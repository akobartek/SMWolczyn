package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmFailed
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.SigningConfirmSuccess
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsScreenEvent.NavigateUp

class ParticipantDetailsViewModel(
    private val meetingId: Int,
    private val email: String,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<Participant>() {

    private val _events = Channel<ParticipantDetailsScreenEvent>()
    val events = _events.receiveAsFlow()

    init {
        setSuccessState()
    }

    fun confirmUserSigning() {
        val participant = (screenState.value as? State.Success)?.data ?: return
        viewModelScope.launch(Dispatchers.Main) {
            setLoadingState()
            meetingsRepository.saveParticipant(
                meetingId = meetingId,
                participant = participant.copy(
                    consents = true,
                    underageConsents = true,
                    paid = true,
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
            meetingsRepository.checkPreviousSigning(meetingId, email)?.let { participant ->
                _screenState.update { State.Success(participant) }
            } ?: onLoadingFailure()
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