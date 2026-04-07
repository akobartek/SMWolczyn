package pl.kapucyni.wolczyn.app.auth.domain.model

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.user_type_admin
import smwolczyn.composeapp.generated.resources.user_type_member
import smwolczyn.composeapp.generated.resources.user_type_organisation

@Serializable
enum class UserType(val stringRes: StringResource) {
    MEMBER(Res.string.user_type_member),
    ORGANISATION(Res.string.user_type_organisation),
    ADMIN(Res.string.user_type_admin),
    ;

    fun allowPermits() = this == ORGANISATION
}