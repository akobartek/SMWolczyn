package pl.kapucyni.wolczyn.app.common.utils

import androidx.navigation.NavHostController
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import kotlin.reflect.KClass

fun NavHostController.navigateSafely(route: Screen, popUpTo: KClass<out Screen>? = null) =
    navigate(route) {
        launchSingleTop = true
        restoreState = true

        popUpTo?.let {
            popUpTo(it) {
                inclusive = true
                saveState = true
            }
        }
    }

fun NavHostController.navigateUpSafely(source: Screen) =
    if (currentDestination?.route?.contains(source::class.simpleName ?: "") == true)
        popBackStack()
    else false