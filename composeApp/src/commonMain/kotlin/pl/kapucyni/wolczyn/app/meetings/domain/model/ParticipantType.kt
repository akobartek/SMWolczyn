package pl.kapucyni.wolczyn.app.meetings.domain.model

import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.participant_type_animator
import smwolczyn.composeapp.generated.resources.participant_type_guardian
import smwolczyn.composeapp.generated.resources.participant_type_member
import smwolczyn.composeapp.generated.resources.participant_type_monk
import smwolczyn.composeapp.generated.resources.participant_type_organisation
import smwolczyn.composeapp.generated.resources.participant_type_priest
import smwolczyn.composeapp.generated.resources.participant_type_scout

enum class ParticipantType(val stringRes: StringResource) {
    MEMBER(Res.string.participant_type_member),
    GUARDIAN(Res.string.participant_type_guardian),
    SCOUT(Res.string.participant_type_scout),
    ANIMATOR(Res.string.participant_type_animator),
    PRIEST(Res.string.participant_type_priest),
    MONK(Res.string.participant_type_monk),
    ORGANISATION(Res.string.participant_type_organisation),
    ;

    fun canSelectWorkshops() =
        this == MEMBER || this == GUARDIAN || this == SCOUT || this == ANIMATOR

    fun canBeGroupMember() =
        this == MEMBER || this == SCOUT

    fun canBeAnimator() =
        this == ANIMATOR || this == MONK || this == PRIEST || this == ORGANISATION
}