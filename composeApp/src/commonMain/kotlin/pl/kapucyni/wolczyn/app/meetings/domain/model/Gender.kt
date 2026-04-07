package pl.kapucyni.wolczyn.app.meetings.domain.model

import org.jetbrains.compose.resources.DrawableResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_gender_both
import smwolczyn.composeapp.generated.resources.ic_gender_man
import smwolczyn.composeapp.generated.resources.ic_gender_woman

enum class Gender(val iconRes: DrawableResource) {
    WOMAN(Res.drawable.ic_gender_woman),
    BOTH(Res.drawable.ic_gender_both),
    MAN(Res.drawable.ic_gender_man),
    ;
}