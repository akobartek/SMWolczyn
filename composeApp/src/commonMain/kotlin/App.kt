import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.KoinApplication
import pl.kapucyni.wolczyn.app.admin.presentation.AdminScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextScreen
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ARGUMENT_BREVIARY_DATE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ARGUMENT_BREVIARY_POSITION
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ARGUMENT_MEETING_NUMBER
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ARGUMENT_PRODUCT_ID
import pl.kapucyni.wolczyn.app.common.utils.getBaseModules
import pl.kapucyni.wolczyn.app.common.utils.navigateSafely
import pl.kapucyni.wolczyn.app.common.utils.navigateUpSafely
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreen
import pl.kapucyni.wolczyn.app.decalogue.presentation.DecalogueScreen
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenScreen
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopProductScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopScreen
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookScreen
import pl.kapucyni.wolczyn.app.theme.AppTheme

@Composable
fun App() {
    KoinApplication(application = {
        modules(getBaseModules())
    }) {
        AppContent()
    }
}

@Composable
fun AppContent() {
    AppTheme {
        val navController = rememberNavController()

        Scaffold {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(onTileClick = { navController.navigateSafely(it.navRoute) })
                }
                composable(Screen.Admin.route) {
                    AdminScreen(
                        onBackPressed = { navController.navigateUpSafely(Screen.Admin.route) }
                    )
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
                    val productId = it.arguments?.getString(ARGUMENT_PRODUCT_ID)

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
                composable(Screen.BreviarySelect.route) {
                    BreviarySelectScreen(
                        onBackPressed = { navController.navigateUpSafely(Screen.BreviarySelect.route) },
                        onSelected = { position, date ->
                            navController.navigateSafely(
                                Screen.BreviaryText.breviaryTextRoute(position, date)
                            )
                        }
                    )
                }
                composable(
                    route = Screen.BreviaryText.route,
                    arguments = listOf(
                        navArgument(ARGUMENT_BREVIARY_POSITION) { type = NavType.IntType },
                        navArgument(ARGUMENT_BREVIARY_DATE) { type = NavType.StringType }
                    )
                ) {
                    val position = it.arguments?.getInt(ARGUMENT_BREVIARY_POSITION) ?: 0
                    val date = it.arguments?.getString(ARGUMENT_BREVIARY_DATE) ?: ""

                    BreviaryTextScreen(
                        onBackPressed = { navController.navigateUpSafely(Screen.BreviaryText.route) },
                        position = position,
                        date = date
                    )
                }
                composable(Screen.Archive.route) {
                    ArchiveScreen(
                        onBackPressed = { navController.navigateUpSafely(Screen.Archive.route) },
                        onMeetingClick = {
                            navController.navigateSafely(Screen.ArchiveMeeting.meetingRoute(it))
                        }
                    )
                }
                composable(
                    route = Screen.ArchiveMeeting.route,
                    arguments =
                    listOf(navArgument(ARGUMENT_MEETING_NUMBER) { type = NavType.IntType })
                ) {
                    val meetingNumber = it.arguments?.getInt(ARGUMENT_MEETING_NUMBER) ?: 0

                    ArchiveMeetingScreen(
                        onBackPressed = { navController.navigateUpSafely(Screen.ArchiveMeeting.route) },
                        meetingNumber = meetingNumber
                    )
                }
            }
        }
    }
}