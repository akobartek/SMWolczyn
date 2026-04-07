package pl.kapucyni.wolczyn.app.auth.domain.model

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.user_permit_animators
import smwolczyn.composeapp.generated.resources.user_permit_signings
import smwolczyn.composeapp.generated.resources.user_permit_volunteers

@Serializable
enum class UserPermit(val stringRes: StringResource) {
    SIGNINGS(Res.string.user_permit_signings),
    VOLUNTEERS(Res.string.user_permit_volunteers),
    ANIMATORS(Res.string.user_permit_animators),
    ;
}