import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import pl.kapucyni.wolczyn.app.admin.presentation.AdminScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveScreen
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreen
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySaveScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextScreen
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.Screen.*
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.utils.navigateSafely
import pl.kapucyni.wolczyn.app.common.utils.navigateUpSafely
import pl.kapucyni.wolczyn.app.core.presentation.AppViewModel
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreen
import pl.kapucyni.wolczyn.app.decalogue.presentation.DecalogueScreen
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenScreen
import pl.kapucyni.wolczyn.app.quiz.presentation.QuizScreen
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopProductScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopScreen
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookScreen
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.workshops.WorkshopsScreen

@Composable
fun App(viewModel: AppViewModel = koinInject()) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    AppTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        ObserveAsEvents(
            flow = SnackbarController.events,
            key1 = snackbarHostState,
        ) { event ->
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()

                val result = snackbarHostState.showSnackbar(
                    message = getString(event.message),
                    actionLabel = event.action?.let { getString(it.name) },
                    withDismissAction = event.action == null,
                    duration = SnackbarDuration.Short,
                )

                if (result == SnackbarResult.ActionPerformed) {
                    event.action?.action?.invoke()
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            NavHost(
                navController = navController,
                startDestination = Home,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                composable<Home> {
                    HomeScreen(
                        onTileClick = { navController.navigateSafely(it.navRoute) },
                        openAccountScreen = {
                            if (user == null)
                                navController.navigateSafely(SignIn())
                            // TODO: else
                        },
                    )
                }

                composable<SignIn> {
                    val screen = it.toRoute<SignIn>()

                    SignInScreen(
                        navigateUp = { navController.navigateUpSafely(screen) },
                        openSignUp = { email -> navController.navigateSafely(SignUp(email)) },
                        viewModel = koinInject { parametersOf(screen.email) },
                    )
                }

                composable<SignUp> {
                    val screen = it.toRoute<SignUp>()

                    SignUpScreen(
                        navigateUp = { navController.navigateUpSafely(screen) },
                        openSignIn = { email ->
                            navController.navigateSafely(
                                route = SignIn(email),
                                popUpTo = SignIn::class,
                            )
                        },
                        viewModel = koinInject { parametersOf(screen.email) },
                    )
                }

                composable<Admin> {
                    AdminScreen(
                        onBackPressed = { navController.navigateUpSafely(Admin) },
                    )
                }

                composable<Schedule> {
                    ScheduleScreen(
                        onBackPressed = { navController.navigateUpSafely(Schedule) },
                        navigateTo = { navController.navigateSafely(it) },
                    )
                }

                composable<SongBook> {
                    SongBookScreen(
                        onBackPressed = { navController.navigateUpSafely(SongBook) },
                    )
                }

                composable<Kitchen> {
                    KitchenScreen(
                        onBackPressed = { navController.navigateUpSafely(Kitchen) },
                        onOpenQuiz = { navController.navigateSafely(Quiz(it)) },
                    )
                }

                composable<Shop> {
                    ShopScreen(
                        onBackPressed = { navController.navigateUpSafely(Shop) },
                        onProductClick = { navController.navigateSafely(ShopProduct(it)) },
                    )
                }
                composable<ShopProduct> {
                    val screen = it.toRoute<ShopProduct>()

                    ShopProductScreen(
                        productId = screen.productId,
                        onBackPressed = { navController.navigateUpSafely(screen) },
                    )
                }

                composable<Decalogue> {
                    DecalogueScreen(
                        onBackPressed = { navController.navigateUpSafely(Decalogue) },
                    )
                }

                composable<BreviarySelect> {
                    BreviarySelectScreen(
                        onBackPressed = { navController.navigateUpSafely(BreviarySelect) },
                        onSelected = { position, date ->
                            navController.navigateSafely(BreviaryText(position, date))
                        },
                        onSaveBreviary = { date ->
                            navController.navigateSafely(BreviarySave(date))
                        },
                    )
                }

                composable<BreviaryText> {
                    val screen = it.toRoute<BreviaryText>()

                    BreviaryTextScreen(
                        onBackPressed = { navController.navigateUpSafely(screen) },
                        position = screen.position,
                        date = screen.date,
                    )
                }

                composable<BreviarySave> {
                    val screen = it.toRoute<BreviarySave>()

                    BreviarySaveScreen(
                        onBackPressed = { navController.navigateUpSafely(screen) },
                        date = screen.date,
                    )
                }

                composable<Archive> {
                    ArchiveScreen(
                        onBackPressed = { navController.navigateUpSafely(Archive) },
                        onMeetingClick = {
                            navController.navigateSafely(ArchiveMeeting(meetingNumber = it))
                        },
                    )
                }

                composable<ArchiveMeeting> {
                    val screen = it.toRoute<ArchiveMeeting>()

                    ArchiveMeetingScreen(
                        onBackPressed = { navController.navigateUpSafely(screen) },
                        meetingNumber = screen.meetingNumber,
                    )
                }

                composable<Quiz> {
                    val screen = it.toRoute<Quiz>()
                    if (screen.type.isBlank()) {
                        navController.navigateUp()
                        return@composable
                    }

                    QuizScreen(
                        quizType = screen.type,
                        onBackPressed = { navController.navigateUpSafely(screen) },
                    )
                }

                composable<Workshops> {
                    WorkshopsScreen(
                        onBackPressed = { navController.navigateUpSafely(Workshops) },
                    )
                }
            }
        }
    }
}