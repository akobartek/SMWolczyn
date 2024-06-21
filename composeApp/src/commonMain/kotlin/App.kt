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
import pl.kapucyni.wolczyn.app.archive.di.archiveModule
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveScreen
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreen
import pl.kapucyni.wolczyn.app.schedule.di.scheduleModule
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleScreen
import pl.kapucyni.wolczyn.app.songbook.di.songBookModule
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookScreen
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.utils.navigateSafely
import pl.kapucyni.wolczyn.app.common.utils.navigateUpSafely
import pl.kapucyni.wolczyn.app.decalogue.presentation.DecalogueScreen
import pl.kapucyni.wolczyn.app.kitchen.di.kitchenModule
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenScreen
import pl.kapucyni.wolczyn.app.shop.di.shopModule
import pl.kapucyni.wolczyn.app.shop.presentation.ShopProductScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(scheduleModule, songBookModule, kitchenModule, shopModule, archiveModule)
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
                        HomeScreen(onTileClick = { navController.navigateSafely(it.navRoute) })
                    }
                    composable(Screen.Schedule.route) {
                        ScheduleScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.Schedule.route) },
                            navigateTo = { navController.navigateSafely(it.navRoute) }
                        )
                    }
                    composable(Screen.SongBook.route) {
                        SongBookScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.SongBook.route) }
                        )
                    }
                    composable(Screen.Kitchen.route) {
                        KitchenScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.Kitchen.route) }
                        )
                    }
                    composable(Screen.Shop.route) {
                        ShopScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.Shop.route) },
                            onProductClick = {
                                navController.navigateSafely(Screen.ShopProduct.productRoute(it))
                            }
                        )
                    }
                    composable(Screen.ShopProduct.route) {
                        val productId = it.arguments?.getString("productId")
                        ShopProductScreen(
                            productId = productId,
                            onBackPressed = { navController.navigateUpSafely(Screen.ShopProduct.route) }
                        )
                    }
                    composable(Screen.Decalogue.route) {
                        DecalogueScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.Decalogue.route) }
                        )
                    }
                    composable(Screen.Breviary.route) {
                        // TODO()
                    }
                    composable(Screen.Archive.route) {
                        ArchiveScreen(
                            onBackPressed = { navController.navigateUpSafely(Screen.Archive.route) }
                        )
                    }
                }
            }
        }
    }
}