import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreen
import pl.kapucyni.wolczyn.app.schedule.di.scheduleModule
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleScreen
import pl.kapucyni.wolczyn.app.songbook.di.songBookModule
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookScreen
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.decalogue.presentation.DecalogueScreen
import pl.kapucyni.wolczyn.app.kitchen.di.kitchenModule
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(scheduleModule, songBookModule, kitchenModule)
    }) {
        AppTheme {
            val navController = rememberNavController()
            Scaffold { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.padding(innerPadding)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(onTileClick = { navController.navigate(it.navRoute) })
                    }
                    composable(Screen.Schedule.route) {
                        ScheduleScreen(
                            onBackPressed = { navController.navigateUp() },
                            navigateTo = { navController.navigate(it.navRoute) }
                        )
                    }
                    composable(Screen.SongBook.route) {
                        SongBookScreen(onBackPressed = { navController.navigateUp() })
                    }
                    composable(Screen.Kitchen.route) {
                        KitchenScreen(onBackPressed = { navController.navigateUp() })
                    }
                    composable(Screen.Shop.route) {
                        // TODO()
                    }
                    composable(Screen.Decalogue.route) {
                        DecalogueScreen(onBackPressed = { navController.navigateUp() })
                    }
                    composable(Screen.Breviary.route) {
                        // TODO()
                    }
                    composable(Screen.Archive.route) {
                        // TODO()
                    }
                }
            }
        }
    }
}