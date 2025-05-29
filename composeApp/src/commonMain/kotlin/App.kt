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
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import pl.kapucyni.wolczyn.app.admin.presentation.AdminScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingScreen
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveScreen
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileScreen
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreen
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySaveScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectScreen
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextScreen
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Admin
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Archive
import pl.kapucyni.wolczyn.app.common.presentation.Screen.ArchiveMeeting
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Auth
import pl.kapucyni.wolczyn.app.common.presentation.Screen.BreviarySave
import pl.kapucyni.wolczyn.app.common.presentation.Screen.BreviarySelect
import pl.kapucyni.wolczyn.app.common.presentation.Screen.BreviaryText
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Decalogue
import pl.kapucyni.wolczyn.app.common.presentation.Screen.EditProfile
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Home
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Kitchen
import pl.kapucyni.wolczyn.app.common.presentation.Screen.MeetingGroups
import pl.kapucyni.wolczyn.app.common.presentation.Screen.MeetingParticipants
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Meetings
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Quiz
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Schedule
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Shop
import pl.kapucyni.wolczyn.app.common.presentation.Screen.ShopProduct
import pl.kapucyni.wolczyn.app.common.presentation.Screen.SignIn
import pl.kapucyni.wolczyn.app.common.presentation.Screen.SignUp
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Signings
import pl.kapucyni.wolczyn.app.common.presentation.Screen.SongBook
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Workshops
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.utils.navigateSafely
import pl.kapucyni.wolczyn.app.common.utils.navigateUpSafely
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreen
import pl.kapucyni.wolczyn.app.core.presentation.composables.ForceUpdateDialog
import pl.kapucyni.wolczyn.app.decalogue.presentation.DecalogueScreen
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenScreen
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.MeetingsScreen
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreen
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsScreen
import pl.kapucyni.wolczyn.app.quiz.presentation.QuizScreen
import pl.kapucyni.wolczyn.app.schedule.presentation.ScheduleScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopProductScreen
import pl.kapucyni.wolczyn.app.shop.presentation.ShopScreen
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookScreen
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.workshops.WorkshopsScreen

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(appViewModel: AppViewModel = koinViewModel()) {
    val appConfiguration by appViewModel.appConfiguration.collectAsStateWithLifecycle()
    val user by appViewModel.user.collectAsStateWithLifecycle()

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

        ForceUpdateDialog(appConfiguration = appConfiguration)

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
                        appConfiguration = appConfiguration,
                        user = user,
                        navigate = { navController.navigateSafely(it) },
                        handleAuthAction = { appViewModel.handleAction(it) },
                    )
                }

                navigation<Auth>(startDestination = SignIn()) {
                    composable<SignIn> {
                        val screen = it.toRoute<SignIn>()

                        SignInScreen(
                            navigateUp = { navController.navigateUpSafely(screen) },
                            openSignUp = { email ->
                                navController.navigateSafely(SignUp(email))
                            },
                            viewModel = koinViewModel { parametersOf(screen.email) },
                        )
                    }

                    composable<SignUp> {
                        val screen = it.toRoute<SignUp>()

                        SignUpScreen(
                            navigateUp = { navController.navigateUpSafely(screen) },
                            openSignIn = { email ->
                                navController.navigateSafely(
                                    route = SignIn(email),
                                    popUpTo = Auth,
                                )
                            },
                            viewModel = koinViewModel { parametersOf(screen.email) },
                        )
                    }
                }

                composable<EditProfile> {
                    EditProfileScreen(
                        navigateUp = { navController.navigateUpSafely(EditProfile) },
                        viewModel = koinViewModel { parametersOf(user) },
                    )
                }

                composable<Signings> {
                    val screen = it.toRoute<Signings>()

                    appConfiguration?.openSigning?.let { meetingId ->
                        SigningsScreen(
                            navigateUp = { navController.navigateUpSafely(screen) },
                            viewModel = koinViewModel {
                                parametersOf(
                                    meetingId,
                                    if (screen.isAdmin.not()) user else null,
                                )
                            }
                        )
                    } ?: navController.popBackStack()
                }

                composable<Meetings> {
                    user?.userType?.let { userType ->
                        MeetingsScreen(
                            navigateUp = { navController.navigateUpSafely(Meetings) },
                            navigate = { navController.navigateSafely(it) },
                            userType = userType,
                            openSigningMeeting = appConfiguration?.openSigning,
                        )
                    } ?: navController.popBackStack()
                }

                composable<MeetingParticipants> {
                    val screen = it.toRoute<MeetingParticipants>()

                    ParticipantsScreen(
                        navigateUp = { navController.navigateUpSafely(screen) },
                        navigate = { destination -> navController.navigateSafely(destination) },
                        viewModel = koinViewModel { parametersOf(screen.meetingId) },
                    )
                }

                composable<MeetingGroups> {
                    val screen = it.toRoute<MeetingGroups>()
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