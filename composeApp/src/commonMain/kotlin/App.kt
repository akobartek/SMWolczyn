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
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.theme.Screen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules()
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
                        HomeScreen(
                            onTileClick = { navController.navigate(it.navRoute) }
                        )
                    }
                    composable(Screen.Schedule.route) {

                    }
                    composable(Screen.SongBook.route) {

                    }
                    composable(Screen.Kitchen.route) {

                    }
                    composable(Screen.Shop.route) {

                    }
                    composable(Screen.Decalogue.route) {

                    }
                    composable(Screen.Breviary.route) {

                    }
                    composable(Screen.Archive.route) {

                    }
                }
            }
        }
    }
}