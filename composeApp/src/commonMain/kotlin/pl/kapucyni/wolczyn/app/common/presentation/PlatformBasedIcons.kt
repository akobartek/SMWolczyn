package pl.kapucyni.wolczyn.app.common.presentation

import org.jetbrains.compose.resources.DrawableResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_arror_back_android
import smwolczyn.composeapp.generated.resources.ic_arrow_back_ios

val navigateUpIcon: DrawableResource = when (navigateUpIconType) {
    NavigateUpIconType.ANDROID -> Res.drawable.ic_arror_back_android
    NavigateUpIconType.IOS -> Res.drawable.ic_arrow_back_ios
}

expect val navigateUpIconType: NavigateUpIconType

enum class NavigateUpIconType {
    ANDROID,
    IOS,
    ;
}