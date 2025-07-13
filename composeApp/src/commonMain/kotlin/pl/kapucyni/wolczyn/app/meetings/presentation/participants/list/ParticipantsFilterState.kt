package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import org.jetbrains.compose.resources.StringResource
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.sorting_alphabetically
import smwolczyn.composeapp.generated.resources.sorting_birthday_asc
import smwolczyn.composeapp.generated.resources.sorting_birthday_desc

data class ParticipantsFilterState(
    val query: String = "",
    val sorting: ParticipantsSorting = ParticipantsSorting.ALPHABETICALLY,
    val onlyConfirmedParticipants: Boolean = false,
    val participantTypes: List<ParticipantType> = ParticipantType.entries.toList(),
    val selectedTypes: List<ParticipantType> = emptyList(),
    val workshops: List<String> = emptyList(),
    val selectedWorkshops: List<String> = emptyList(),
)

enum class ParticipantsSorting(val stringRes: StringResource) {
    ALPHABETICALLY(Res.string.sorting_alphabetically),
    BIRTHDAY_ASC(Res.string.sorting_birthday_asc),
    BIRTHDAY_DESC(Res.string.sorting_birthday_desc),
    ;
}