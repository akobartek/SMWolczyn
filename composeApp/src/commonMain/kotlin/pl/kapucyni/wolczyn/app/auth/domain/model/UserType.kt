package pl.kapucyni.wolczyn.app.auth.domain.model

import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.user_type_admin
import smwolczyn.composeapp.generated.resources.user_type_animators
import smwolczyn.composeapp.generated.resources.user_type_member
import smwolczyn.composeapp.generated.resources.user_type_scouts
import smwolczyn.composeapp.generated.resources.user_type_signings

enum class UserType(val stringRes: StringResource) {
    MEMBER(Res.string.user_type_member),
    SIGNINGS_MANAGER(Res.string.user_type_signings),
    SCOUTS_MANAGER(Res.string.user_type_scouts),
    ANIMATORS_MANAGER(Res.string.user_type_animators),
    ADMIN(Res.string.user_type_admin),
    ;

    fun canManageParticipants() =
        this == ADMIN || this == SIGNINGS_MANAGER
}