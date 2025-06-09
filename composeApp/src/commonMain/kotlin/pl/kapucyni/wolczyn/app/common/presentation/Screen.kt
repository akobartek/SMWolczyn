package pl.kapucyni.wolczyn.app.common.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object Home : Screen

    @Serializable
    data object Auth : Screen

    @Serializable
    data class SignIn(val email: String = "") : Screen

    @Serializable
    data class SignUp(val email: String = "") : Screen

    @Serializable
    data object EditProfile : Screen

    @Serializable
    data class Signings(
        val meetingId: Int? = null,
        val isAdmin: Boolean = false,
        val email: String? = null,
    ) : Screen

    @Serializable
    data object Meetings : Screen

    @Serializable
    data class MeetingParticipants(val meetingId: Int) : Screen

    @Serializable
    data class ParticipantDetails(
        val meetingId: Int,
        val email: String,
    ) : Screen

    @Serializable
    data class MeetingGroups(val meetingId: Int) : Screen

    @Serializable
    data object Admin : Screen

    @Serializable
    data object Schedule : Screen

    @Serializable
    data object SongBook : Screen

    @Serializable
    data object Kitchen : Screen

    @Serializable
    data object Decalogue : Screen

    @Serializable
    data object Shop : Screen

    @Serializable
    data class ShopProduct(val productId: String) : Screen

    @Serializable
    data object BreviarySelect : Screen

    @Serializable
    data class BreviaryText(val position: Int, val date: String) : Screen

    @Serializable
    data class BreviarySave(val date: String) : Screen

    @Serializable
    data object Archive : Screen

    @Serializable
    data class ArchiveMeeting(val meetingNumber: Int) : Screen

    @Serializable
    data class Quiz(val type: String) : Screen

    @Serializable
    data object Workshops : Screen
}